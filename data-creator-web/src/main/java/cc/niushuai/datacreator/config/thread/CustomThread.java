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

package cc.niushuai.datacreator.config.thread;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * cv了spring的线程处理机制
 * @see org.springframework.scheduling.concurrent.CustomizableThreadFactory
 * @author niushuai233
 * @date 2024/09/05 11:44
 * @since 0.0.1
 */
public class CustomThread {

    public static class Factory extends Creator {

        /**
         * Create a new Factory with default thread name prefix.
         */
        public Factory() {
            super();
        }

        /**
         * Create a new Factory with the given thread name prefix.
         *
         * @param threadNamePrefix the prefix to use for the names of newly created threads
         */
        public Factory(String threadNamePrefix) {
            super(threadNamePrefix);
        }


        @Override
        public Thread newThread(Runnable runnable) {
            return createThread(runnable);
        }
    }


    /**
     * Simple customizable helper class for creating new {@link Thread} instances.
     * Provides various bean properties: thread name prefix, thread priority, etc.
     *
     * <p>Serves as base class for thread factories such as
     * {@link org.springframework.scheduling.concurrent.CustomizableThreadFactory}.
     *
     * @author Juergen Hoeller
     * @see org.springframework.scheduling.concurrent.CustomizableThreadFactory
     * @since 2.0.3
     */

    @SuppressWarnings("serial")
    public static class Creator implements ThreadFactory, Serializable {

        private static final Logger log = LoggerFactory.getLogger(Creator.class);

        private String threadNamePrefix;

        private int threadPriority = Thread.NORM_PRIORITY;

        private boolean daemon = false;

        @Nullable
        private ThreadGroup threadGroup;

        private final AtomicInteger threadCount = new AtomicInteger();


        /**
         * Create a new CustomThreadCreator with default thread name prefix.
         */
        public Creator() {
            this.threadNamePrefix = getDefaultThreadNamePrefix();
        }

        /**
         * Create a new CustomThreadCreator with the given thread name prefix.
         *
         * @param threadNamePrefix the prefix to use for the names of newly created threads
         */
        public Creator(@Nullable String threadNamePrefix) {
            this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
        }


        /**
         * Specify the prefix to use for the names of newly created threads.
         * Default is "SimpleAsyncTaskExecutor-".
         */
        public void setThreadNamePrefix(@Nullable String threadNamePrefix) {
            this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
        }

        /**
         * Return the thread name prefix to use for the names of newly
         * created threads.
         */
        public String getThreadNamePrefix() {
            return this.threadNamePrefix;
        }

        /**
         * Set the priority of the threads that this factory creates.
         * Default is 5.
         *
         * @see java.lang.Thread#NORM_PRIORITY
         */
        public void setThreadPriority(int threadPriority) {
            this.threadPriority = threadPriority;
        }

        /**
         * Return the priority of the threads that this factory creates.
         */
        public int getThreadPriority() {
            return this.threadPriority;
        }

        /**
         * Set whether this factory is supposed to create daemon threads,
         * just executing as long as the application itself is running.
         * <p>Default is "false": Concrete factories usually support explicit cancelling.
         * Hence, if the application shuts down, Runnables will by default finish their
         * execution.
         * <p>Specify "true" for eager shutdown of threads which still actively execute
         * a {@link Runnable} at the time that the application itself shuts down.
         *
         * @see java.lang.Thread#setDaemon
         */
        public void setDaemon(boolean daemon) {
            this.daemon = daemon;
        }

        /**
         * Return whether this factory should create daemon threads.
         */
        public boolean isDaemon() {
            return this.daemon;
        }

        /**
         * Specify the name of the thread group that threads should be created in.
         *
         * @see #setThreadGroup
         */
        public void setThreadGroupName(String name) {
            this.threadGroup = new ThreadGroup(name);
        }

        /**
         * Specify the thread group that threads should be created in.
         *
         * @see #setThreadGroupName
         */
        public void setThreadGroup(@Nullable ThreadGroup threadGroup) {
            this.threadGroup = threadGroup;
        }

        /**
         * Return the thread group that threads should be created in
         * (or {@code null} for the default group).
         */
        @Nullable
        public ThreadGroup getThreadGroup() {
            return this.threadGroup;
        }


        /**
         * Template method for the creation of a new {@link Thread}.
         * <p>The default implementation creates a new Thread for the given
         * {@link Runnable}, applying an appropriate thread name.
         *
         * @param runnable the Runnable to execute
         * @see #nextThreadName()
         */
        public Thread createThread(Runnable runnable) {
            Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
            thread.setPriority(getThreadPriority());
            thread.setDaemon(isDaemon());
            return thread;
        }

        /**
         * Return the thread name to use for a newly created {@link Thread}.
         * <p>The default implementation returns the specified thread name prefix
         * with an increasing thread count appended: e.g. "SimpleAsyncTaskExecutor-0".
         *
         * @see #getThreadNamePrefix()
         */
        protected String nextThreadName() {
            String nextThreadName = getThreadNamePrefix() + "-" + this.threadCount.incrementAndGet();
            log.debug("create new thread with name: {}", nextThreadName);
            return nextThreadName;
        }

        /**
         * Build the default thread name prefix for this factory.
         *
         * @return the default thread name prefix (never {@code null})
         */
        protected String getDefaultThreadNamePrefix() {
            return ClassUtils.getShortName(getClass()) + "-";
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return createThread(runnable);
        }
    }

}
