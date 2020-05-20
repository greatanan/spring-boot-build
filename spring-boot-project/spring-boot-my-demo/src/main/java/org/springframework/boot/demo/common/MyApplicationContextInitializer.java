package org.springframework.boot.demo.common;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyApplicationContextInitializer implements ApplicationContextInitializer{

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		applicationContext.getEnvironment().getPropertySources();
		System.out.println("-----MyApplicationContextInitializer initialize-----");
	}

}
