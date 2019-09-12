package com.yzy.common.config;

import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @title:
 * @description: SchedulerConfig  spring简单定时任务配置
 *
 * @package: com.yzy.common.config.SchedulerConfig.java
 * @param:
 * @return:
 * @author: yzy
 * @date: 2019-09-12 08:25:28
 * @version: v1.0
 */


@Configuration
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer {

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        schedulerFactoryBean.setStartupDelay(2);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
    }
}