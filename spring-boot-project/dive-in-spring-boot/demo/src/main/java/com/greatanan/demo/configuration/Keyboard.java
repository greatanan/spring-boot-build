package com.greatanan.demo.configuration;

import com.greatanan.demo.domain.Computer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: chuanlei.guo
 * @date: 2020/09/29
 */
@ConditionalOnMissingBean(Computer.class)
@Component
public class Keyboard {
	public Keyboard() {
		System.out.println("test");
	}
}
