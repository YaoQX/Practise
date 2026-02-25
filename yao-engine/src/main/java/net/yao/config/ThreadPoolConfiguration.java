package net.yao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync // 异步注解
@Configuration
public class ThreadPoolConfiguration {

    @Bean("YaoExecutor") // 给线程池起个Bean名字
    public ThreadPoolTaskExecutor stressEngineExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 1. 核心线程数：压测平台属于 IO 密集型（频繁发请求等响应），一般设置为 CPU 核心数的 2 倍
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);

        // 2. 最大线程数 缓冲队列要是满了，就创建核心线程数以外的线程
        executor.setMaxPoolSize(64);

        // 3. 队列容量：缓冲队列
        executor.setQueueCapacity(1024);

        // 4. 存活时间：60秒，之后就要销毁线程
        executor.setKeepAliveSeconds(60);

        // 5. 线程名字前缀 以便定义
        executor.setThreadNamePrefix("stress-engine-async-");

        // 6. 拒绝策略：使用 CallerRunsPolicy。
        // 如果压测任务提交过快，队列满了，就让提交任务的 Tomcat 主线程自己去执行，防止丢数据
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化
        executor.initialize();

        return executor;
    }
}
