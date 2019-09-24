package com.yzy.common.service;

import com.yzy.common.domain.TaskDO;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

/**
 * @title: 
 * @description:
 * 
 * @package: com.yzy.common.service.JobService.java
 * @author: yzy
 * @date: 2019-09-23 09:11:00
 * @version: v1.0
 */
public interface JobService {

    TaskDO get(Long id);

    List<TaskDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(TaskDO taskScheduleJob);

    int update(TaskDO taskScheduleJob);

    int remove(Long id);

    int batchRemove(Long[] ids);

    void initSchedule() throws SchedulerException;

    void changeStatus(Long jobId, String cmd) throws SchedulerException;

    void updateCron(Long jobId) throws SchedulerException;
}
