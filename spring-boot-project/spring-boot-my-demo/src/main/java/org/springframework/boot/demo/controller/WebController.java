package org.springframework.boot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.demo.service.WebService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebController {

	private String saywhat = "hello";

	@Autowired
	private WebService webService;

	@RequestMapping("/web")
	public String web(){
		return webService.hello();
	}

	@Override
	public String toString(){
		return "qqqq";
	}

}
