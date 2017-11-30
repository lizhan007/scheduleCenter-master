package com.shanhy.schedulecenter.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob implements Job {

	public MyJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Hello World!  MyJob is executing.");
	}
}