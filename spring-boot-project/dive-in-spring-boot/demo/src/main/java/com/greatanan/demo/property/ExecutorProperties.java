package com.greatanan.demo.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 @description:
 @author: guochuanlei
 @date: 2021/11/12
 */
@ConfigurationProperties(prefix = "com.greatanan.executor.async")
public class ExecutorProperties {

	/**
	 * 核心线程数
	 */
	private int corePoolSize;

	/**
	 * 最大线程数
	 */
	private int maxPoolSize;

	/**
	 * 队列大小
	 */
	private int queueCapacity;

	/**
	 * 保持存活时间（秒）
	 */
	private int keepAliveSeconds;

	/**
	 * 线程名前缀
	 */
	private String threadNamePrefix;

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public String getThreadNamePrefix() {
		return threadNamePrefix;
	}

	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
	}

}
