package com.greatanan.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnifiedExceptionHandlingController {

	/**
	 * 测试统一异常处理
	 */
	@RequestMapping("/testUnifiedExceptionHandling")
	public String test() {
		int i = 10 / 0;
		return "success";
	}



}
