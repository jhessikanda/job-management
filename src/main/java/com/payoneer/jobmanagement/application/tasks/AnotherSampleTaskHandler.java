package com.payoneer.jobmanagement.application.tasks;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnotherSampleTaskHandler implements Job {

    private static Logger LOGGER = LoggerFactory.getLogger(AnotherSampleTaskHandler.class);

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        LOGGER.info("Running AnotherSampleTaskHandler Job !!");
    }
}