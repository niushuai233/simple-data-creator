/*
 * Copyright (C) 2023 niushuai233 niushuai.cc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.niushuai.datacreator.config.log;

import ch.qos.logback.classic.pattern.Abbreviator;
import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import ch.qos.logback.classic.pattern.LoggerConverter;
import ch.qos.logback.classic.pattern.TargetLengthBasedClassNameAbbreviator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.OptionHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 复制NamedConverter逻辑，结尾添加log输出方法与输出行号
 *
 * @author niushuai233
 * @date 2024/09/09 17:48
 * @see ch.qos.logback.classic.pattern.NamedConverter
 * @since 0.0.1
 */
public class CallerDetailConvert extends LoggerConverter {

    private static final String DISABLE_CACHE_SYSTEM_PROPERTY = "logback.CallerDetailConvert.disableCache";

    private static final int INITIAL_CACHE_SIZE = 512;
    // this is the JDK implementation default
    private static final double LOAD_FACTOR = 0.75;

    /**
     * We don't let the cache map size to go over MAX_ALLOWED_REMOVAL_THRESHOLD
     * elements
     */
    private static final int MAX_ALLOWED_REMOVAL_THRESHOLD = (int) (2048 * LOAD_FACTOR);

    /**
     * When the cache miss rate is above 30%, the cache is deemed inefficient.
     */
    private static final double CACHE_MISSRATE_TRIGGER = 0.3d;

    /**
     * We should have a sample size of minimal length before computing the cache
     * miss rate.
     */
    private static final int MIN_SAMPLE_SIZE = 1024;

    private static final double NEGATIVE = -1;
    private final CallerDetailConvert.NameCache cache = new CallerDetailConvert.NameCache(INITIAL_CACHE_SIZE);
    private volatile boolean cacheEnabled = true;
    private Abbreviator abbreviator = null;

    private volatile int cacheMisses = 0;
    private volatile int totalCalls = 0;

    /**
     * Gets fully qualified name from event.
     *
     * @param event The LoggingEvent to process, cannot not be null.
     * @return name, must not be null.
     */
    @Override
    protected String getFullyQualifiedName(final ILoggingEvent event) {
        StackTraceElement callerStackTrace = event.getCallerData()[0];
        String loggerName = event.getLoggerName();
        String method = callerStackTrace.getMethodName();
        int lineNumber = callerStackTrace.getLineNumber();
        return loggerName + "." + method + ":" + lineNumber;
    }

    @Override
    public void start() {

        String disableCacheProp = OptionHelper.getSystemProperty(DISABLE_CACHE_SYSTEM_PROPERTY);
        boolean disableCache = OptionHelper.toBoolean(disableCacheProp, false);
        if (disableCache) {
            addInfo("Disabling name cache via System.properties");
            this.cacheEnabled = false;
        }

        String optStr = getFirstOption();
        if (optStr != null) {
            try {
                int targetLen = Integer.parseInt(optStr);
                if (targetLen == 0) {
                    abbreviator = new ClassNameOnlyAbbreviator();
                } else if (targetLen > 0) {
                    abbreviator = new TargetLengthBasedClassNameAbbreviator(targetLen);
                }
            } catch (NumberFormatException nfe) {
                addError("failed to parse integer string [" + optStr + "]", nfe);
            }
        }
        super.start();
    }

    @Override
    public String convert(ILoggingEvent event) {
        String fqn = this.getFullyQualifiedName(event);

        if (abbreviator == null) {
            return fqn;
        } else {
            if (cacheEnabled) {
                return viaCache(fqn);
            } else {
                return abbreviator.abbreviate(fqn);
            }
        }
    }

    /**
     * This method is synchronized. It is the only place where the cache, a subclass
     * of LinkedHashMap, is modified.
     * <p>
     * The cache can be cleared via a call to disableCache(). However, the call to
     * disableCache() is made indirectly from within viaCache(String).
     *
     * @param fqn
     * @return
     */
    private synchronized String viaCache(String fqn) {
        totalCalls++;
        String abbreviated = cache.get(fqn);
        if (abbreviated == null) {
            cacheMisses++;
            abbreviated = abbreviator.abbreviate(fqn);
            cache.put(fqn, abbreviated);
        }
        return abbreviated;
    }

    private void disableCache() {
        if (!cacheEnabled)
            return;
        this.cacheEnabled = false;
        cache.clear();
        addInfo("Disabling cache at totalCalls=" + totalCalls);
    }

    @Override
    public double getCacheMissRate() {
        return cache.cacheMissCalculator.getCacheMissRate();
    }

    @Override
    public int getCacheMisses() {
        return cacheMisses;
    }

    private class NameCache extends LinkedHashMap<String, String> {

        private static final long serialVersionUID = 1050866539278406045L;

        int removalThreshold;
        CallerDetailConvert.CacheMissCalculator cacheMissCalculator = new CallerDetailConvert.CacheMissCalculator();

        NameCache(int initialCapacity) {
            super(initialCapacity);
            this.removalThreshold = (int) (initialCapacity * LOAD_FACTOR);
        }

        /**
         * In the JDK tested, this method is called for every map insertion.
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> entry) {
            if (shouldDoubleRemovalThreshold()) {
                removalThreshold *= 2;

                int missRate = (int) (cacheMissCalculator.getCacheMissRate() * 100);

                CallerDetailConvert.this.addInfo("Doubling nameCache removalThreshold to " + removalThreshold
                        + " previous cacheMissRate=" + missRate + "%");
                cacheMissCalculator.updateMilestones();
            }

            if (size() >= removalThreshold) {
                return true;
            } else
                return false;
        }

        private boolean shouldDoubleRemovalThreshold() {

            double rate = cacheMissCalculator.getCacheMissRate();

            // negative rate indicates insufficient sample size
            if (rate < 0)
                return false;

            if (rate < CACHE_MISSRATE_TRIGGER)
                return false;

            // cannot double removalThreshold is already at max allowed size
            if (this.removalThreshold >= MAX_ALLOWED_REMOVAL_THRESHOLD) {
                CallerDetailConvert.this.disableCache();
                return false;
            }

            return true;
        }
    }

    class CacheMissCalculator {

        int totalsMilestone = 0;
        int cacheMissesMilestone = 0;

        void updateMilestones() {
            this.totalsMilestone = CallerDetailConvert.this.totalCalls;
            this.cacheMissesMilestone = CallerDetailConvert.this.cacheMisses;
        }

        double getCacheMissRate() {

            int effectiveTotal = CallerDetailConvert.this.totalCalls - totalsMilestone;

            if (effectiveTotal < MIN_SAMPLE_SIZE) {
                // cache miss rate cannot be negative. With a negative value, we signal the
                // caller of insufficient sample size.
                return NEGATIVE;
            }

            int effectiveCacheMisses = CallerDetailConvert.this.cacheMisses - cacheMissesMilestone;
            return (1.0d * effectiveCacheMisses / effectiveTotal);
        }
    }
}
