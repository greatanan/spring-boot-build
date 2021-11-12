package com.greatanan.autoConfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 @description:
 @author: guochuanlei
 @date: 2021/11/12
 */
@Data
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

}
