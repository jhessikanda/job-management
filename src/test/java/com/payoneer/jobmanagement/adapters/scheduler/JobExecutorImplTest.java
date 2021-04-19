package com.payoneer.jobmanagement.adapters.scheduler;

import com.payoneer.jobmanagement.application.tasks.AnotherSampleTaskHandler;
import com.payoneer.jobmanagement.application.tasks.SampleTaskHandler;
import com.payoneer.jobmanagement.domain.exceptions.JobSchedulerException;
import com.payoneer.jobmanagement.domain.model.Job;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JobExecutorImplTest {
    private JobExecutorImpl jobExecutorImpl;

    @Before
    public void setUp() {
        jobExecutorImpl = new JobExecutorImpl(getJobBeans());
    }


    @Test(expected = JobSchedulerException.class)
    public void invalidHandlerName() throws JobSchedulerException {
        Job job = new Job();
        job.setDescription("Job not scheduled");
        job.setEnabled(true);
        job.setHandler("InvalidHandlerName");
        job.setPriority(1);

        jobExecutorImpl.execute(job);
    }

    private Map<String, org.quartz.Job> getJobBeans() {
        Map<String, org.quartz.Job> beans = new HashMap();
        beans.put("SampleTaskHandler", new SampleTaskHandler());
        beans.put("AnotherSampleTaskHandler", new AnotherSampleTaskHandler());
        return beans;
    }
}
