package org.example.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义异步任务线程池
 * @author zhoudashuai
 * @date 2022年01月06日 10:55 下午
 */
@Slf4j
@EnableAsync //开启异步任务支持
@Configuration
public class AsyncPoolConfig implements AsyncConfigurer {

    /**
     * 将自定义的线程池注入到spring容器中
     * @return
     */
    @Bean
    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("Zgb-Async-");

        //等待所有任务完成再关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.AbortPolicy()
        );

        //初始化线程池,初始化core线程
        executor.initialize();
        return executor;
    }

    /**
     * 指定系统中的异步任务在出现异常时使用到的处理器
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * 异步任务异常捕获处理器
     */
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler{
        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            ex.printStackTrace();
            log.error("Async Error: [{}],Method: [{}],Param,[{}]",
                    ex.getMessage(),method.getName(),
                    JSON.toJSONString(params));
        }
    }
}
