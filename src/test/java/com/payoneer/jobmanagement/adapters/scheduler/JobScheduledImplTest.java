package com.payoneer.jobmanagement.adapters.scheduler;

import com.payoneer.jobmanagement.application.tasks.AnotherSampleTaskHandler;
import com.payoneer.jobmanagement.application.tasks.SampleTaskHandler;
import com.payoneer.jobmanagement.domain.exceptions.JobSchedulerException;
import com.payoneer.jobmanagement.domain.model.Job;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JobScheduledImplTest {

    private JobScheduledImpl jobScheduledImpl;

    @Before
    public void setUp() {
        jobScheduledImpl = new JobScheduledImpl(getJobBeans());
    }


    @Test(expected = JobSchedulerException.class)
    public void invalidHandlerName() throws JobSchedulerException {
        Job job = new Job();
        job.setDescription("Job scheduled");
        job.setEnabled(true);
        job.setHandler("InvalidHandlerName");
        job.setPriority(1);
        job.setScheduler("0 * * * * ?");

        jobScheduledImpl.execute(job);
    }

    private Map<String, org.quartz.Job> getJobBeans() {
        Map<String, org.quartz.Job> beans = new HashMap();
        beans.put("SampleTaskHandler", new SampleTaskHandler());
        beans.put("AnotherSampleTaskHandler", new AnotherSampleTaskHandler());
        return beans;
    }
}
