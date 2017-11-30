package com.shanhy.schedulecenter;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;

import com.shanhy.schedulecenter.jobs.MyJob;

@SpringBootApplication
public class ScheduleCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleCenterApplication.class, args);

//		try {
//			// Grab the Scheduler instance from the Factory
//			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//
//			// and start it off
//			scheduler.start();
//
//			// define the job and tie it to our MyJob class
//			JobDetail job = new Job(MyJob.class).withIdentity("job1", "group1").build();
//
//			// Trigger the job to run now, and then repeat every 40 seconds
//			Trigger trigger = new Trigger().withIdentity("trigger1", "group1").startNow()
//					.withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();
//
//			// Tell quartz to schedule the job using our trigger
//			scheduler.scheduleJob(job, trigger);
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
	}
}
