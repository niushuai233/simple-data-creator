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

package cc.niushuai.datacreator.config.thread;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池自定义配置
 *
 * @author niushuai233
 * @date 2024/09/05 9:58
 * @since 0.0.1
 */
public class CustomThreadPoolTaskCustomizer implements ThreadPoolTaskExecutorCustomizer {

    private static final Logger log = LoggerFactory.getLogger(CustomThreadPoolTaskCustomizer.class);

    /**
     * spring加载的线程池配置
     */
    private TaskExecutionProperties properties;

    /**
     * 自定义线程前缀
     */
    private String threadNamePrefix;

    public CustomThreadPoolTaskCustomizer(TaskExecutionProperties properties) {
        this.properties = properties;
    }

    public CustomThreadPoolTaskCustomizer(TaskExecutionProperties properties, String threadNamePrefix) {
        this.properties = properties;
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public void customize(ThreadPoolTaskExecutor taskExecutor) {

        if (!"task-".equals(properties.getThreadNamePrefix())) {
            threadNamePrefix = properties.getThreadNamePrefix();
        }

        // set pool properties
        TaskExecutionProperties.Pool pool = properties.getPool();
        // 核心线程数
        taskExecutor.setCorePoolSize(pool.getCoreSize());
        // 最大线程数
        taskExecutor.setMaxPoolSize(pool.getMaxSize());
        // 队列大小
        int poolQueueCapacity = pool.getQueueCapacity();
        if (Integer.MAX_VALUE == poolQueueCapacity) {
            taskExecutor.setQueueCapacity(32);
            log.warn("thread [{}] reset pool queue capacity to 32", threadNamePrefix);
        } else {
            taskExecutor.setQueueCapacity(poolQueueCapacity);
        }
        // 是否允许核心线程在超时后销毁
        taskExecutor.setAllowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        // 空闲线程保活时间
        Duration keepAlive = pool.getKeepAlive();
        taskExecutor.setKeepAliveSeconds((int) keepAlive.getSeconds());

        // 线程池终止策略
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        Duration awaitTerminationPeriod = shutdown.getAwaitTerminationPeriod();
        taskExecutor.setAwaitTerminationSeconds(null == awaitTerminationPeriod ? 60 : (int) awaitTerminationPeriod.getSeconds());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(shutdown.isAwaitTermination());

        // 自定义线程创建
        taskExecutor.setThreadFactory(new CustomThread.Factory(threadNamePrefix));
        // 线程池中的线程的名称前缀
//        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        // 当pool size达到max size的时候, 新任务处理策略
        // 执行 由调用者所在的线程来执行 目的是为了让所有的任务全部都完成
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 不执行 丢弃任务 并抛出RejectedExecutionException异常
//        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 不执行 丢弃当前任务 不会抛出异常
//        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 执行 丢弃队列起始位置的任务
//        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 初始化
        taskExecutor.initialize();
    }
}
