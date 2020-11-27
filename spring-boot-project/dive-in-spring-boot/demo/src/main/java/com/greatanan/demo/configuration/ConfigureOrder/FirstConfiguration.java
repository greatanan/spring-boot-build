package com.greatanan.demo.configuration.ConfigureOrder;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: chuanlei.guo
 * @date: 2020/11/27
 */
@AutoConfigureBefore(B_SecondConfiguration.class)
@Configuration
public class FirstConfiguration {

	public FirstConfiguration() {
		System.out.println("配置类FirstConfiguration的构造方法执行了...");
	}

}
