package com.greatanan.autoconfigue.configuration;

import com.greatanan.autoconfigue.annotation.EnableHelloWorld;

/**
 * HelloWorld 自动装配
 */
//@Configuration // Spring 模式注解装配  这个注解不加也是可以的
@EnableHelloWorld // Spring @Enable 模块装配
public class HelloWorldAutoConfiguration {
}
