package com.yzy.common.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @title: 
 * @description: 
 * 
 * @package: com.yzy.common.task.TestJob.java
 * @author: yzy
 * @date: 2019-09-12 08:36:43
 * @version: v1.0
 */

@Component
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("===========================" + context.getFireTime().toString() + "============================");
        System.out.println("this is TestJob !");


    }
}
