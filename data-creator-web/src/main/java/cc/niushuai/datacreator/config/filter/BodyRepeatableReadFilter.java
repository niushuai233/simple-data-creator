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

import cc.niushuai.datacreator.config.filter.wrapper.BodyRepeatableReadRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 注入filter 使body可重复读生效
 *
 * @author niushuai233
 * @date 2024/09/09 14:50
 * @since 0.0.1
 */
@Slf4j
public class BodyRepeatableReadFilter implements Filter {

    public BodyRepeatableReadFilter() {
        log.info("add body repeatable read filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        BodyRepeatableReadRequestWrapper requestWrapper = new BodyRepeatableReadRequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }
}
