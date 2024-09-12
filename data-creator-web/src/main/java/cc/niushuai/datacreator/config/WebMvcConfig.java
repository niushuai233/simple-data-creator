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

package cc.niushuai.datacreator.config;

import cc.niushuai.datacreator.config.interceptor.AddTraceIdResponseInterceptor;
import cc.niushuai.datacreator.config.interceptor.TtlContextParserInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * web mvc 配置类
 *
 * @author niushuai233
 * @date 2024/08/14 13:51
 * @since 0.0.1
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * 跨域设置
     *
     * @param registry
     * @return
     * @author niushuai233
     * @date 2024/08/14 14:50
     * @since 0.0.1
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOriginPatterns("*")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
                .allowCredentials(true);

        log.info("add CorsMapping for /** with method GET and POST");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 忽略favicon.ico请求
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                if ("GET".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().toString().equals("/favicon.ico")) {
                    // 204 NO_CONTENT
                    response.setStatus(HttpStatus.NO_CONTENT.value());
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/**");

        // parse context
        registry.addInterceptor(new TtlContextParserInterceptor())
                .addPathPatterns("/**");

        // add trace id
        registry.addInterceptor(new AddTraceIdResponseInterceptor())
                .addPathPatterns("/**");
    }


    /**
     * jackson配置
     *
     * @return
     * @author niushuai233
     * @date 2024/08/14 14:50
     * @since 0.0.1
     */
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {

        log.info("reconfigure Jackson2ObjectMapperBuilder");

        return Jackson2ObjectMapperBuilder.json()
                // bean为null时不抛异常
                .failOnEmptyBeans(false)
                // 未知属性时不抛异常
                .failOnUnknownProperties(false)
                // 时区与pattern
                .locale(Locale.getDefault())
                .timeZone(TimeZone.getDefault())
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                // ALWAYS 默认策略，任何情况都执行序列化
                // NON_NULL 属性为 NULL 不序列化
                // NON_EMPTY  null、集合数组等没有内容、空字符串等，都不会被序列化
                // NON_DEFAULT  如果字段是默认值，就不会被序列化
                // NON_ABSENT  null的不会序列化，但如果类型是AtomicReference，依然会被序列化
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                // 序列化时的排序按照字母属性
                //.featuresToEnable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
                // 禁止时间序列化为时间戳
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
