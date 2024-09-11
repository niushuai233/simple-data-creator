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

package cc.niushuai.datacreator.config.thread;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Executor.class, ThreadPoolTaskExecutor.class})
@EnableConfigurationProperties(TaskExecutionProperties.class)
public class ThreadConfig {

    public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = "taskExec";
    public static final String APPLICATION_TASK_EXECUTOR_BEAN_NAME = "applicationTaskExec";
    public static final String CORE_EXECUTOR = "coreExec";
    public static final String SCHEDULED_EXECUTOR = "scheExec";
    public static final String TTL_CORE_EXECUTOR = "ttlCoreExec";
    public static final String TTL_SCHEDULED_EXECUTOR = "ttlScheExec";

    @Lazy
    @Bean(CORE_EXECUTOR)
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolTaskExecutorBuilder builder, TaskExecutionProperties properties) {
        return builder.customizers(new CustomThreadPoolTaskCustomizer(properties, CORE_EXECUTOR)).build();
    }

    @Lazy
    @Bean(SCHEDULED_EXECUTOR)
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(ThreadPoolTaskSchedulerBuilder builder, TaskSchedulingProperties properties) {
        return builder.customizers(new CustomThreadPoolScheduleCustomizer(properties, SCHEDULED_EXECUTOR)).build();
    }

    @Lazy
    @Bean(TTL_CORE_EXECUTOR)
    public ExecutorService executorService(@Qualifier(CORE_EXECUTOR) ThreadPoolTaskExecutor executor) {
        return TtlExecutors.getTtlExecutorService(executor.getThreadPoolExecutor());
    }

    @Lazy
    @Bean(TTL_SCHEDULED_EXECUTOR)
    public ScheduledExecutorService scheduledExecutorService(@Qualifier(SCHEDULED_EXECUTOR) ThreadPoolTaskScheduler schedulerExecutor) {
        return TtlExecutors.getTtlScheduledExecutorService(schedulerExecutor.getScheduledExecutor());
    }


    @Configuration(proxyBeanMethods = false)
    @AutoConfigureAfter(ThreadConfig.class)
    public static class AsyncConfig implements AsyncConfigurer {

        @Value("${spring.task.async.pool.coreSize:8}")
        private Integer coreSize;
        @Value("${spring.task.async.pool.maxSize:32}")
        private Integer maxSize;
        @Value("${spring.task.async.pool.queueCapacity:2}")
        private Integer queueCapacity;
        @Value("${spring.task.async.threadNamePrefix:asyncExecutor}")
        private String threadNamePrefix;
        @Value("${spring.task.async.concurrencyLimit:2}")
        private Integer concurrencyLimit;

        @Override
        public Executor getAsyncExecutor() {

            TaskExecutionProperties properties = new TaskExecutionProperties();
            TaskExecutionProperties.Pool pool = properties.getPool();
            // 核心数
            pool.setCoreSize(coreSize);
            // 最大数
            pool.setMaxSize(maxSize);
            // 允许线程池动态扩缩
            pool.setAllowCoreThreadTimeout(true);
            // 随便写的
            pool.setQueueCapacity(queueCapacity);
            pool.setKeepAlive(Duration.ofMinutes(1));

            TaskExecutionProperties.Simple simple = properties.getSimple();
            simple.setConcurrencyLimit(concurrencyLimit);

            TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
            shutdown.setAwaitTermination(true);
            shutdown.setAwaitTerminationPeriod(Duration.ofMinutes(1));

            ThreadPoolTaskExecutor build = new ThreadPoolTaskExecutorBuilder()
                    .acceptTasksAfterContextClose(false)
                    .customizers(new CustomThreadPoolTaskCustomizer(properties, threadNamePrefix))
                    .build();
            return TtlExecutors.getTtlExecutor(build);
        }
    }
}