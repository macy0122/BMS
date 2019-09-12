package com.yzy.common.task;

import com.yzy.oa.domain.Response;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
/**
 * @title: 
 * @description: 
 * 
 * @package: com.yzy.common.task.WelcomeJob.java
 * @author: yzy
 * @date: 2019-09-12 08:36:43
 * @version: v1.0
 */

@Component
public class WelcomeJob implements Job {
    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        template.convertAndSend("/topic/getResponse", new Response("欢迎体验bms,这是一个任务计划，使用了websocket和quzrtz技术，可以在计划列表中取消!"));
    }

}