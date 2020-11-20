package com.greatanan.demo;

import com.greatanan.demo.common.ApplicationContextUtil;
import com.greatanan.demo.common.MyApplicationContextInitializer;
import com.greatanan.demo.service.WebService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @Description:
 * @Author: chuanlei.guo
 * @Date: 2020/11/20
 */
@SpringBootApplication
public class MySpringBootApplication {

	public static void main(String[] args) {

		//SpringApplication.run(MySpringBootApplication.class, args);
		SpringApplication application = new SpringApplication(MySpringBootApplication.class);
		application.addInitializers(new MyApplicationContextInitializer());

		application.run(args);

		ApplicationContext context = ApplicationContextUtil.getApplicationContext();
		WebService webService = (WebService) context.getBean("webService");

		System.out.println(webService.hello());

	}
}
