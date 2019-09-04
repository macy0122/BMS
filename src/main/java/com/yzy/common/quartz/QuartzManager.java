package com.yzy.common.quartz;

import cn.hutool.core.util.ClassUtil;
import com.yzy.common.domain.ScheduleJob;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @title: QuartzManager.java
 * @description: 计划任务管理
 */
@Service
public class QuartzManager {
    public final Logger log = LoggerFactory.getLogger(QuartzManager.class);
    // private SchedulerFactoryBean schedulerFactoryBean
    // =SpringContextHolder.getBean(SchedulerFactoryBean.class);
    // @Autowired
    // @Qualifier("schedulerFactoryBean")
    // private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private Scheduler scheduler;

    /**
     * 添加任务
     *
     * @throws SchedulerException
     */

    public void addJob(ScheduleJob job) {
        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类

            Class<Job> jobClass = ClassUtil.loadClass(job.getBeanClass());
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(job.getJobName(), job.getJobGroup())// 任务名称和组构成任务key
                    .build();
            // 定义调度触发规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())// 触发器key
                    .startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).startNow().build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleJob> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                ScheduleJob job = new ScheduleJob();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleJob> getRunningJob() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            ScheduleJob job = new ScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 立即执行job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void runAJobNow(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void updateJobCron(ScheduleJob scheduleJob) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 使用SimpleScheduleBuilder创建一个定时任务
     *
     * @param jobName          job名称
     * @param jobGroupName     job分组名称
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param jobClass         job的class名
     * @param durability       是否持久化
     * @param interval         间隔时间，单位秒
     * @param delay            延迟启动时间，单位秒
     * @return boolean
     */
    public Boolean addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass,
                          boolean durability, int interval, int delay) {
        /*
         * 1.取到任务调度器Scheduler
         * 2.定义jobDetail;
         * 3.定义trigger;
         * 4.使用Scheduler添加任务;
         */
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).storeDurably(durability).build();

            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(interval).repeatForever();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(scheduleBuilder)
                    .startAt(new Date(System.currentTimeMillis() + delay * 1000))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 使用CronScheduleBuilder创建一个定时任务
     *
     * @param jobName          job名称
     * @param jobGroupName     job分组名称
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param jobClass         job的class名
     * @param durability       是否持久化
     * @param cronExpression   定时表达式
     * @param delay            延迟启动时间，单位秒
     * @return
     */
    public Boolean addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass,
                          boolean durability, String cronExpression, int delay) {
        /*
         * 1.取到任务调度器Scheduler
         * 2.定义jobDetail;
         * 3.定义trigger;
         * 4.使用Scheduler添加任务;
         */
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).storeDurably(durability).build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .startAt(new Date(System.currentTimeMillis() + delay * 1000))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 动态停止job任务
     *
     * @param jobName      job名称
     * @param jobGroupName job分组名称
     * @return
     */
    public Boolean pause(String jobName, String jobGroupName) {
        try {
            JobKey key = new JobKey(jobName, jobGroupName);
            scheduler.pauseJob(key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 动态开始job任务
     *
     * @param jobName      job名称
     * @param jobGroupName job分组名称
     * @return
     */
    public Boolean start(String jobName, String jobGroupName) {
        try {
            JobKey key = new JobKey(jobName, jobGroupName);
            scheduler.resumeJob(key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 动态修改任务执行的时间
     *
     * @param type             创建类型，1-cron,2-simple
     * @param jobName          job名称
     * @param jobGroupName     job分组名称
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param time             type为1时-填cron表达式，为2-整数
     * @return
     */
    public Boolean modify(String type, String jobName, String jobGroupName, String triggerName, String triggerGroupName, String time) {

        try {
            Integer status = getJobStatus(triggerName, triggerGroupName);
            if (status == 0) {
                //NONE - 0,该job不存在
                log.warn("NONE - 0,该job不存在");
                return null;
            }

            //获取任务
            JobKey key = new JobKey(jobName, jobGroupName);
            //获取jobDetail
            JobDetail jobDetail = scheduler.getJobDetail(key);
            //生成trigger
            if ("1".equals(type)) {
                // 1.CronSchedule
                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerName, triggerGroupName)
                        .withSchedule(CronScheduleBuilder.cronSchedule(time))
                        .build();
                //删除旧的任务，否则报错
                //scheduler.deleteJob(key);
                delete(jobName, jobGroupName, triggerName, triggerGroupName);
                //重新启动任务
                scheduler.scheduleJob(jobDetail, trigger);
                return true;
            }

            if ("2".equals(type)) {
                // 2.SimpleSchedule
                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerName, triggerGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.valueOf(time)))
                        .build();
                //删除旧的任务，否则报错
                //scheduler.deleteJob(key);
                delete(jobName, jobGroupName, triggerName, triggerGroupName);
                //重新启动任务
                scheduler.scheduleJob(jobDetail, trigger);
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查看job运行状态
     *
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器分组名称
     * @return TriggerState枚举值： NONE - 0；NORMAL - 1；PAUSED - 2；COMPLETE - 3；ERROR - 4；BLOCKED - 5
     */
    public Integer getJobStatus(String triggerName, String triggerGroupName) {

        /*
            TriggerState枚举值
            NONE - 0,
            NORMAL -1,
            PAUSED - 2,
            COMPLETE - 3,
            ERROR - 4,
            BLOCKED - 5;
        */
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            return scheduler.getTriggerState(triggerKey).ordinal();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除定时任务
     *
     * @param jobName          job名称
     * @param jobGroupName     job分组名称
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器分组名称
     * @return
     */
    public Boolean delete(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Integer status = getJobStatus(triggerName, triggerGroupName);
            if (status == 0) {
                //NONE - 0,该job不存在
                log.warn("NONE - 0,该job不存在");
                return null;
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);//通过触发器名和组名获取TriggerKey
            JobKey jobKey = new JobKey(jobName, jobGroupName); //通过任务名和组名获取JobKey
            scheduler.pauseTrigger(triggerKey);    //停止触发器
            scheduler.unscheduleJob(triggerKey);//移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 启动所有定时任务
     */
    public Boolean startAll() {
        try {
            scheduler.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭所有定时任务
     */
    public Boolean shutdownAll() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}