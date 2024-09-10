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

package cc.niushuai.datacreator.config;

import cc.niushuai.datacreator.config.filter.BodyRepeatableReadFilter;
import cc.niushuai.datacreator.config.filter.TraceToMDCFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * filter注册机
 *
 * @author niushuai233
 * @date 2024/09/09 14:44
 * @since 0.0.1
 */
@Configuration
public class FilterConfig {


    /**
     * 注册TraceToMDCFilter过滤器
     */
    @Bean
    public FilterRegistrationBean<TraceToMDCFilter> traceToMDCFilter() {
        FilterRegistrationBean<TraceToMDCFilter> registration = new FilterRegistrationBean<>();
        registration.setName(TraceToMDCFilter.class.getSimpleName());
        registration.setFilter(new TraceToMDCFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    /**
     * 注册BodyRepeatableReadFilter过滤器
     */
    @Bean
    public FilterRegistrationBean<BodyRepeatableReadFilter> bodyRepeatableReadFilter() {
        FilterRegistrationBean<BodyRepeatableReadFilter> registration = new FilterRegistrationBean<>();
        registration.setName(BodyRepeatableReadFilter.class.getSimpleName());
        registration.setFilter(new BodyRepeatableReadFilter());
        registration.addUrlPatterns("/*");
//        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setOrder(0);
        return registration;
    }
}
