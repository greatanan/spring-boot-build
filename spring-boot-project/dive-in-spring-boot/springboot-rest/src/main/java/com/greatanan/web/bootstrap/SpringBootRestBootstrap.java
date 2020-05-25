package com.greatanan.web.bootstrap;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {
		"com.greatanan.web.controller",
		"com.greatanan.web.config"
})
public class SpringBootRestBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestBootstrap.class, args);
	}
}
