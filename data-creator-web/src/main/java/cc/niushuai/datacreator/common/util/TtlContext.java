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

package cc.niushuai.datacreator.common.util;

import cc.niushuai.datacreator.common.exception.BizException;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * thread local context
 *
 * @author niushuai233
 * @date 2024/09/05 9:42
 * @since 0.0.1
 */
@Slf4j
public class TtlContext {

    private static final TransmittableThreadLocal<Context> context = new TransmittableThreadLocal();

    public static void remove() {
        if (null != context.get()) {
            context.remove();
            return;
        }
        log.warn("ttl context is null ==> {}", ExceptionUtil.getPrintStackTraceInfo());
        context.remove();
    }

    private static void test() {
        try {
            Assert.notNull(context, "context is null");
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }

    public static void set(String key, Object value) {
        if (null == context.get()) {
            context.set(new Context());
        }

        context.get().extraMap.put(key, value);
    }

    private static Object get(String key) {
        test();
        return context.get().extraMap.get(key);
    }

    private static Object nullableGet(String key) {
        return context.get();
    }

    public static String nullableGetTraceId() {
        return null == context.get() ? Constants.DEFAULT : context.get().extraMap.get(Constants.TRACE_ID) + "";
    }

    static class Context {
        private Map<String, Object> extraMap = new HashMap<String, Object>(2);
    }
}
