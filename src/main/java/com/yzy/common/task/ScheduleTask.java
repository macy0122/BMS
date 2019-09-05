package com.yzy.common.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author yzy
 */
@Component
public class ScheduleTask {

	/**
	 * 定时任务2min执行一次
	 */
	@Scheduled(fixedRate = 120000)
	public void scheduleTask(){
		System.out.println("这是一个定时任务!"+ Calendar.getInstance().getTime().toString());
	}
}
