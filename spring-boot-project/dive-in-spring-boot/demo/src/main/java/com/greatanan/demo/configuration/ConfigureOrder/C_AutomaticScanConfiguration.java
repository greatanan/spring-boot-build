package com.greatanan.demo.configuration.ConfigureOrder;

import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: chuanlei.guo
 * @date: 2020/11/27
 */
@Configuration
public class C_AutomaticScanConfiguration {

	public C_AutomaticScanConfiguration() {
		System.out.println("我是被自动扫描进来的配置类, 配置类C_AutomaticScanConfiguration的构造方法执行了...");
	}

}
