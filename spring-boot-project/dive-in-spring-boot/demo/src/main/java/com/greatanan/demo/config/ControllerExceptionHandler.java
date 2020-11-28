package com.greatanan.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @description: 统一处理Controller抛出的异常
 * @author: chuanlei.guo
 * @date: 2020/11/20
 */
@ControllerAdvice
@Component
public class ControllerExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	/**
	 * 算术异常
	 *
	 * @param exception 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = ArithmeticException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String handleArithmeticExceptionException(ArithmeticException exception) {
		log.error(exception.getMessage(), exception);
		return "被统一异常处理中的handleArithmeticExceptionException方法拦截了";
	}


	/**
	 * 未定义异常
	 *
	 * @param exception 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String handleException(Exception exception) {
		log.error(exception.getMessage(), exception);
		return "被统一异常处理中的handleException方法拦截了";
	}

}





