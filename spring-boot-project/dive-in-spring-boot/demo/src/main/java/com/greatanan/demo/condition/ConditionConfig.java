package com.greatanan.demo.condition;

import com.greatanan.demo.domain.Computer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: chuanlei.guo
 * @date: 2020/11/25
 */
@Configuration
public class ConditionConfig {

	@Bean(name = "notebookPC")
	public Computer computer1() {
		return new Computer("笔记本电脑");
	}

	@ConditionalOnMissingBean(Computer.class)
	@Bean("reservePC")
	public Computer computer2() {
		return new Computer("备用电脑");
	}

}
