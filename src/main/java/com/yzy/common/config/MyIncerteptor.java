package com.yzy.common.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @title:
 * @description:  自定义拦截器
 * @package: com.yzy.common.config
 * @ClassName: com.yzy.common.config.MyIncerteptor.java
 * @author: yzy
 * @date: 2019/9/10 9:29
 * @param:
 * @return:
 * @version: v1.0
 */

public class MyIncerteptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("进入拦截器");
		System.out.println(request.getRequestURL().toString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
