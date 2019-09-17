package com.yzy;

import com.github.xiaoymin.knife4j.spring.annotations.EnableSwaggerBootstrapUi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @title:
 * @description: springboot 启动类
 * @package: com.yzy.BmsApplication.java
 * @author: yzy
 * @date: 2019-09-11 17:42:12
 * @version: v1.0
 */

@EnableCaching
@EnableScheduling
@EnableAsync
@EnableSwagger2
@EnableSwaggerBootstrapUi
@EnableTransactionManagement
@ServletComponentScan   //Servlet、Filter、Listener 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。
@MapperScan("com.yzy.*.dao")
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class BmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BmsApplication.class, args);
		System.out.println("启动成功");
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		UndertowServletWebServerFactory undertow=new UndertowServletWebServerFactory();
		return undertow;
	}
}



