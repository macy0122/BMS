package com.yzy.common.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @title:
 * @description:
 *
 * @package: com.yzy.common.task.ScheduleTask.java
 * @author: yzy
 * @date: 2019-09-12 08:36:01
 * @version: v1.0
 */
@Component
public class ScheduleTask {

	/**
	 * @title:
	 * @description: 定时任务2min执行一次
	 *
	 * @param: []
	 * @return: void
	 */
	@Scheduled(fixedRate = 120000)
	public void scheduleTask(){
		System.out.println("这是一个定时任务!"+ Calendar.getInstance().getTime().toString());
	}
}
