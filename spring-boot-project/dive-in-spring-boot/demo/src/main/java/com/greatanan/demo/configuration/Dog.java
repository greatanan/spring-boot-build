package com.greatanan.demo.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@ConditionalOnBean(name = "mouse")
@Component
public class Dog {
}
