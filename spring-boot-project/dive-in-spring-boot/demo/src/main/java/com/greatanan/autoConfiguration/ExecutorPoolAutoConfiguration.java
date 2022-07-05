package com.greatanan.autoConfiguration;

import com.greatanan.demo.property.ExecutorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 @description:
 @author: guochuanlei
 @date: 2021/11/12
 */
@Configuration
@EnableConfigurationProperties(ExecutorProperties.class)
public class ExecutorPoolAutoConfiguration {

	@Bean
	public EcAsyncConfigurer asyncConfigurer(ExecutorProperties executorProperties) {
		return new EcAsyncConfigurer(executorProperties);
	}

	/**
	 * 自定义实现异步线程池配置
	 */
	@Slf4j
	public static class EcAsyncConfigurer implements AsyncConfigurer {

		private final ExecutorProperties executorProperties;

		public EcAsyncConfigurer(ExecutorProperties executorProperties) {
			this.executorProperties = executorProperties;
		}

		/**
		 * 这个方法执行需要在启动类上面加@EnableAsync
		 */
		@Override
		public Executor getAsyncExecutor() {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setCorePoolSize(executorProperties.getCorePoolSize());
			executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
			executor.setWaitForTasksToCompleteOnShutdown(true);
			executor.setAwaitTerminationSeconds(executorProperties.getKeepAliveSeconds());
			executor.setThreadNamePrefix(executorProperties.getThreadNamePrefix());
			executor.setQueueCapacity(executorProperties.getQueueCapacity());
			executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			executor.setRejectedExecutionHandler((r, e) -> {
				// 设置拒绝策略
			});
			executor.initialize();

			return executor;
		}

		@Override
		public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
			return (e, m, params) -> {

			};
		}

	}


}
