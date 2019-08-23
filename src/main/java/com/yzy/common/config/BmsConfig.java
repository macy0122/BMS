package com.yzy.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "yzy")
@Data
public class BmsConfig {
    //上传路径
    private String uploadPath;

    //用户名
    private String username;
    //密码
    private String password;

}