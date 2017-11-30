package com.shanhy.schedulecenter.common;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时任务管理类
 *
 * @author 单红宇(365384722)
 * @myblog http://blog.csdn.net/catoop/
 * @create 2016年8月23日
 */
public class QuartzManager {
	
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "SHY_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "SHY_TRIGGERGROUP_NAME";

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName
	 *            任务名
	 * @param cls
	 *            任务
	 * @param time
	 *            Cron表达式时间设置，参考quartz说明文档
	 * 
	 * @author SHANHY
	 */
	public static void addJob(String jobName, Class<? extends Job> cls, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();// 任务名，任务组，任务执行类
			// 触发器
			CronTrigger trigger = newTrigger()
					.withIdentity(jobName, TRIGGER_GROUP_NAME)// 触发器名,触发器组
					.withSchedule(cronSchedule(time))
					.build();
			sched.scheduleJob(jobDetail, trigger);
			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * 
	 * @author SHANHY
	 */
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Class<? extends Job> jobClass, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = newJob(jobClass).withIdentity(jobName, jobGroupName).build();// 任务名，任务组，任务执行类
			// 触发器
			CronTrigger trigger = newTrigger()
					.withIdentity(triggerName, triggerGroupName)// 触发器名,触发器组
					.withSchedule(cronSchedule(time))
					.build();
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 * 
	 * @author SHANHY
	 */
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey(jobName, TRIGGER_GROUP_NAME));
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobKey(jobName, JOB_GROUP_NAME));
				Class<? extends Job> objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 * 
	 * @author SHANHY
	 */
	public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey(triggerName, triggerGroupName));
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				// 修改时间
				trigger.getTriggerBuilder().withSchedule(cronSchedule(time)).build();
				// 重启触发器
				sched.resumeTrigger(triggerKey(triggerName, triggerGroupName));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * 
	 * @author SHANHY
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerKey(jobName, TRIGGER_GROUP_NAME));// 停止触发器
			sched.unscheduleJob(triggerKey(jobName, TRIGGER_GROUP_NAME));// 移除触发器
			sched.deleteJob(jobKey(jobName, JOB_GROUP_NAME));// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 * 
	 * @author SHANHY
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerKey(triggerName, triggerGroupName));// 停止触发器
			sched.unscheduleJob(triggerKey(triggerName, triggerGroupName));// 移除触发器
			sched.deleteJob(jobKey(jobName, jobGroupName));// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 启动所有定时任务
	 * 
	 * @author SHANHY
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭所有定时任务
	 * 
	 * @author SHANHY
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}