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

import cc.niushuai.datacreator.common.enums.ErrorCodeEnum;
import cc.niushuai.datacreator.common.exception.BizException;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * thread local context
 *
 * @author niushuai233
 * @date 2024/09/05 9:42
 * @since 0.0.1
 */
@Slf4j
public class TTLContext {

    private static final TransmittableThreadLocal<Context> CONTEXT = new TransmittableThreadLocal();


    public static void set(Context context) {
        CONTEXT.set(context);
    }

    public static Context get() {
        Context context = CONTEXT.get();
        if (null == context) {
            throw new BizException(ErrorCodeEnum.TTL_CONTENT_NOT_FOUND, ExceptionUtil.getPrintStackTraceInfo());
        }
        return context;
    }


    public static Context nullableGet() {
        Context context = CONTEXT.get();
        if (null == context) {
            return null;
        }
        return context;
    }

    public static String nullableGetTraceId() {
        Context context = nullableGet();
        return null == context ? null : context.getTraceId();
    }

    public static String nullableGetTraceId(String defaultId) {
        return StrUtil.nullToDefault(nullableGetTraceId(), defaultId);
    }

    public static void remove() {
        if (null != CONTEXT.get()) {
            CONTEXT.remove();
            return;
        }
        CONTEXT.remove();
        log.warn("ttl context is null ==> {}", ExceptionUtil.getPrintStackTraceInfo());
    }

    @Data
    public static class Context {

        // token
        private String token;

        // 用户信息
        private String userId;
        private String username;

        // 追踪问题使用
        private String traceId;
        private String spanId;

    }

}
