package com.yzy.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @title:
 * @description: WebMvcConfigurer自定义配置
 *
 * @package: com.yzy.common.config.MyWebMvcConfigurer.java
 * @param:
 * @return:
 * @author: yzy
 * @date: 2019-09-12 08:28:01
 * @version: v1.0
 */

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    BmsConfig bmsConfig;

    /**
     * @title:
     * @description: 资源注册
     *
     * @param: [registry]
     * @return: void
     * @version: v1.0
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:///" + bmsConfig.getUploadPath());
    }

    /**
     * @title:
     * @description: 拦截器注册
     *
     * @param: [registry]
     * @return: void
     * @version: v1.0
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyIncerteptor()).addPathPatterns("/dtb/**").excludePathPatterns("/test/**","/login/**");
    }
}