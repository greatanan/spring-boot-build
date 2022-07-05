package com.greatanan.myNote;

import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
    真正实现自动配置类的核心类:
                 {@link org.springframework.boot.autoconfigure.AutoConfigurationImportSelector}

    启动内嵌的tomcat:
                 {@link TomcatWebServer#initialize()}

    springboot内嵌tomcat相关的自动配置类
 				{@link org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration}

    SpringFactoriesLoader:
                 {@link SpringFactoriesLoader}

    WebMvcAutoConfiguration:
                 {@link  org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration }

    DispatcherServletAutoConfiguration:
                 {@link DispatcherServletAutoConfiguration}
    RedisAutoConfiguration:
                 {@link RedisAutoConfiguration}














 */
public class MyNote {
}
