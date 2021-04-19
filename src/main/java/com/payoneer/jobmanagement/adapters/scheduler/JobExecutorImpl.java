package com.payoneer.jobmanagement.adapters.scheduler;

import com.payoneer.jobmanagement.domain.exceptions.JobSchedulerException;
import com.payoneer.jobmanagement.domain.model.Job;
import com.payoneer.jobmanagement.domain.ports.JobScheduler;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("immediate_execution_job")
public class JobExecutorImpl implements JobScheduler {

    private static Logger LOGGER = LoggerFactory.getLogger(JobExecutorImpl.class);

    private final Map<String, org.quartz.Job> jobBeans;

    public JobExecutorImpl(Map<String, org.quartz.Job> jobBeans) {
        this.jobBeans = jobBeans;
    }

    @Override
    public void execute(Job job) throws JobSchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler;

        try {
            JobDetail jobDetail = buildJobDetail(job);

            TriggerBuilder trigger = TriggerBuilder.newTrigger()
                    .withIdentity(job.getId() + job.getHandler() + "Trigger")
                    .startNow()
                    .forJob(jobDetail);

            scheduler = factory.getScheduler();

            if (job.getPriority() != null) {
               trigger.withPriority(job.getPriority().intValue());
            }
            SimpleTrigger simpleTrigger = (SimpleTrigger) trigger.build();

            scheduler.scheduleJob(jobDetail, simpleTrigger);
            scheduler.start();

            job.running();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new JobSchedulerException(e.getMessage(), e);
        }
    }

    private JobDetail buildJobDetail(Job job) throws IllegalArgumentException {
        org.quartz.Job jobObj = this.jobBeans.get(job.getHandler());

        if (jobObj == null) {
            throw new IllegalArgumentException("Invalid job handler!");
        }

        return JobBuilder.newJob(jobObj.getClass())
                    .withIdentity(job.getId() + job.getHandler() + "Job")
                    .build();
    }
}
