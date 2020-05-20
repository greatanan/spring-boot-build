package org.springframework.boot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.demo.common.ApplicationContextUtil;
import org.springframework.boot.demo.common.MyApplicationContextInitializer;
import org.springframework.boot.demo.service.WebService;
import org.springframework.context.ApplicationContext;

//@SpringBootApplication 注解 : Spring Boot应用标注在某个类上说明这个类是SpringBoot的主配置类，SpringBoot就应该运行这个类的main方法来启动SpringBoot应用
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
