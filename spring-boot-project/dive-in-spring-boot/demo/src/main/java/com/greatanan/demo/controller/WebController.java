package com.greatanan.demo.controller;

import com.greatanan.demo.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

	@Autowired
	private WebService webService;

	@RequestMapping("/web")
	public String web() {
		return webService.hello();
	}

	@Override
	public String toString() {
		return "qqqq";
	}

	/**
	 * 测试统一异常处理
	 */
	@RequestMapping("/testUnifiedExceptionHandling")
	public String test() {
		int i = 10 / 0;
		return "success";
	}

}
