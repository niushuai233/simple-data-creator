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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池自定义配置
 *
 * @author niushuai233
 * @date 2024/09/05 9:58
 * @since 0.0.1
 */
public class CustomThreadPoolScheduleCustomizer implements ThreadPoolTaskSchedulerCustomizer {

    /**
     * spring加载的线程池配置
     */
    private TaskSchedulingProperties properties;

    /**
     * 自定义线程前缀
     */
    private String threadNamePrefix;

    public CustomThreadPoolScheduleCustomizer(TaskSchedulingProperties properties, String threadNamePrefix) {
        this.properties = properties;
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public void customize(ThreadPoolTaskScheduler taskScheduler) {

        if (!"scheduling-".equals(properties.getThreadNamePrefix())) {
            threadNamePrefix = properties.getThreadNamePrefix();
        }

        // 核心线程数
        taskScheduler.setPoolSize(properties.getPool().getSize());
        // 线程池终止策略
        TaskSchedulingProperties.Shutdown shutdown = properties.getShutdown();

        Duration awaitTerminationPeriod = shutdown.getAwaitTerminationPeriod();
        taskScheduler.setAwaitTerminationSeconds(null == awaitTerminationPeriod ? 60 : (int) awaitTerminationPeriod.getSeconds());
        taskScheduler.setWaitForTasksToCompleteOnShutdown(shutdown.isAwaitTermination());

        taskScheduler.setErrorHandler(new CustomScheduleErrorHandler());
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);

        // 自定义线程创建
        taskScheduler.setThreadFactory(new CustomThread.Factory(threadNamePrefix));
        // 线程池中的线程的名称前缀
//        taskScheduler.setThreadNamePrefix(threadNamePrefix);
        // 当pool size达到max size的时候, 新任务处理策略
        // 执行 由调用者所在的线程来执行 目的是为了让所有的任务全部都完成
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 不执行 丢弃任务 并抛出RejectedExecutionException异常
//        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 不执行 丢弃当前任务 不会抛出异常
//        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 执行 丢弃队列起始位置的任务
//        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 初始化
        taskScheduler.initialize();
    }


    class CustomScheduleErrorHandler implements ErrorHandler {

        private static final Logger log = LoggerFactory.getLogger(CustomScheduleErrorHandler.class);

        @Override
        public void handleError(Throwable t) {
            log.error("Unexpected error occurred in scheduled task ==> ", t);
        }
    }

}
