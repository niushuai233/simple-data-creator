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

package cc.niushuai.datacreator.config.filter;

import cc.niushuai.datacreator.common.util.Constants;
import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * 通过filter往mdc中放入可追踪的traceId
 *
 * @author niushuai233
 * @date 2024/09/10 11:43
 * @since 0.0.1
 */
@Slf4j
public class TraceToMDCFilter implements Filter {

    private static final String BASE_HEX_STRING = "0123456789abcdef";

    public TraceToMDCFilter() {
        log.info("add trace to MDC filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.put(Constants.TRACE_ID, getTraceId());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(Constants.TRACE_ID);
        }
    }

    private String getTraceId() {
        return RandomUtil.randomString(BASE_HEX_STRING, 16);
    }
}
