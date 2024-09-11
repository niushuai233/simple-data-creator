/*
 * Copyright (C) 2019-2024 niushuai233 niushuai.cc
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
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ttl context包装拦截器
 *
 * @author niushuai233
 * @date 2024/09/09 15:06
 * @since 0.0.1
 */
@Slf4j
public class TtlContextParserInterceptor implements HandlerInterceptor {

    public TtlContextParserInterceptor() {
        log.info("add ttl context parser interceptor");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(Constants.TOKEN);
        TtlContext.set(Constants.TOKEN, token);

        String traceId = MDC.get(Constants.TRACE_ID);
        TtlContext.set(Constants.TRACE_ID, traceId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TtlContext.remove();
    }
}
