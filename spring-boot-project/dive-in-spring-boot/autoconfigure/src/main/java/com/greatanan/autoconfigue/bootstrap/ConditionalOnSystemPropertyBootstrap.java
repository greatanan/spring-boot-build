package com.greatanan.autoconfigue.bootstrap;

import com.greatanan.autoconfigue.condition.ConditionalOnSystemProperty;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 系统属性条件引导类
 */
public class ConditionalOnSystemPropertyBootstrap {

    @Bean
    //自定义注解并指定里面的属性值
    @ConditionalOnSystemProperty(name = "user.name", value = "Mercy")
    public String helloWorld() {
        return "Hello,World 小马哥";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ConditionalOnSystemPropertyBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        // 通过名称和类型获取 helloWorld Bean
        String helloWorld = context.getBean("helloWorld", String.class);

        System.out.println("helloWorld Bean : " + helloWorld);

        // 关闭上下文
        context.close();
    }
}
