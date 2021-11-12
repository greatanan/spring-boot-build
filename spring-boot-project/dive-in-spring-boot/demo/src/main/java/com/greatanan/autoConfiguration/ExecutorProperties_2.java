package com.greatanan.autoConfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 @description:
 @author: guochuanlei
 @date: 2021/11/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.greatanan.executor.async2")
public class ExecutorProperties_2 {

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
