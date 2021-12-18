package com.lyj.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/8/14 11:33 上午
 */
@Slf4j
@EnableAsync //开启异步
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        return new ThreadPoolExecutor(
                3, //核心线程数
                8, //最大线程数
                30, //超时时间
                TimeUnit.SECONDS, //超时时间单位
                new ArrayBlockingQueue<>(1000),//存放任务的阻塞队列
                new ThreadPoolExecutor.AbortPolicy());//传入默认的拒绝策略(当任务被拒绝时抛出RejectedExecutionException异常)
    }

    // 异常处理
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("异步任务处理异常", throwable);//记录一下日志
        };
    }
}
