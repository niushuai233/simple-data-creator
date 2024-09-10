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

package cc.niushuai.datacreator.config.interceptor;

import cc.niushuai.datacreator.common.util.Constants;
import cc.niushuai.datacreator.common.util.TtlContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 添加trace-id到响应头中
 *
 * @author niushuai233
 * @date 2024/09/09 14:35
 * @since 0.0.1
 */
@Slf4j
public class AddTraceIdResponseInterceptor implements HandlerInterceptor {

    public AddTraceIdResponseInterceptor() {
        log.info("add traceId response interceptor");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader(Constants.TRACE_ID, TtlContext.nullableGetTraceId());
        return true;
    }
}
