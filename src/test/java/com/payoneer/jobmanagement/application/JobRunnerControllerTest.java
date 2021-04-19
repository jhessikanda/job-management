package com.payoneer.jobmanagement.application;

import com.payoneer.jobmanagement.adapters.scheduler.JobExecutorImpl;
import com.payoneer.jobmanagement.adapters.scheduler.JobScheduledImpl;
import com.payoneer.jobmanagement.domain.exceptions.JobSchedulerException;
import com.payoneer.jobmanagement.domain.model.Job;
import com.payoneer.jobmanagement.domain.model.JobStatus;
import com.payoneer.jobmanagement.domain.ports.JobScheduler;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JobRunnerControllerTest {

    private JobRunnerController jobRunnerController;

    private JobScheduledImpl scheduledJobMock;

    private JobExecutorImpl immediateJobExecutorMock;

    @Before
    public void setUp() {
        scheduledJobMock = mock(JobScheduledImpl.class);
        immediateJobExecutorMock = mock(JobExecutorImpl.class);

        jobRunnerController = new JobRunnerController(getSchedulers());
    }

    @Test
    public void successfulScheduleJobTest() throws JobSchedulerException {
        List<Job> jobs = new ArrayList<>();

        Job job1 = new Job();
        job1.setDescription("Job scheduled");
        job1.setEnabled(true);
        job1.setHandler("SampleTaskHandler");
        job1.setPriority(1);
        job1.setScheduler("0 * * * * ?");

        jobs.add(job1);

        doAnswer(i -> {
            Job job = i.getArgument(0);
            assertNotNull(job.getScheduler());
            return null;
        }).when(scheduledJobMock).execute(any(Job.class));

        Collection<Job> result = jobRunnerController.execute(jobs);

        assertEquals(1, result.size());

        result.forEach(job -> assertEquals(JobStatus.QUEUED, job.getStatus()));
    }

    @Test
    public void successfulExecuteJobTest() throws JobSchedulerException {
        List<Job> jobs = new ArrayList<>();

        Job job2 = new Job();
        job2.setDescription("Job not scheduled");
        job2.setEnabled(true);
        job2.setHandler("AnotherSampleTaskHandler");

        Job job3 = new Job();
        job3.setDescription("Job not scheduled");
        job3.setEnabled(true);
        job3.setHandler("AnotherSampleTaskHandler");
        job3.setPriority(2);

        jobs.add(job2);
        jobs.add(job3);

        doAnswer(i -> {
            Job job = i.getArgument(0);
            assertNull(job.getScheduler());
            return null;
        }).when(scheduledJobMock).execute(any(Job.class));

        Collection<Job> result = jobRunnerController.execute(jobs);

        assertEquals(2, result.size());

        result.forEach(job -> assertEquals(job.getStatus(), JobStatus.SUCCESS));
    }

    @Test
    public void doNotExecuteDisabledJobTest() {
        List<Job> jobs = new ArrayList<>();

        Job job1 = new Job();
        job1.setDescription("Job scheduled");
        job1.setEnabled(false);
        job1.setHandler("InvalidHandlerName");
        job1.setPriority(1);
        job1.setScheduler("0 * * * * ?");

        jobs.add(job1);

        Collection<Job> result = jobRunnerController.execute(jobs);

        verifyZeroInteractions(scheduledJobMock);
        verifyZeroInteractions(immediateJobExecutorMock);
        assertTrue(result.isEmpty());
    }

    @Test
    public void throwErrorWhileExecutingJobTest() throws JobSchedulerException {
        List<Job> jobs = new ArrayList<>();

        Job job1 = new Job();
        job1.setDescription("Job scheduled");
        job1.setEnabled(true);
        job1.setHandler("SampleTaskHandler");
        job1.setPriority(1);
        job1.setScheduler("0 * * * * ?");

        Job job2 = new Job();
        job2.setDescription("Job not scheduled");
        job2.setEnabled(true);
        job2.setHandler("AnotherSampleTaskHandler");

        Job job3 = new Job();
        job3.setDescription("Job not scheduled");
        job3.setEnabled(true);
        job3.setHandler("AnotherSampleTaskHandler");
        job3.setPriority(2);

        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);

        doThrow(JobSchedulerException.class).when(scheduledJobMock).execute(any(Job.class));
        doThrow(JobSchedulerException.class).when(immediateJobExecutorMock).execute(any(Job.class));

        Collection<Job> result = jobRunnerController.execute(jobs);

        assertEquals(3, result.size());

        result.forEach(job -> assertEquals(job.getStatus(), JobStatus.FAILED));
    }

    private Map<String, JobScheduler> getSchedulers() {
        Map<String, JobScheduler> schedulers = new HashMap<>();
        schedulers.put("immediate_execution_job", immediateJobExecutorMock);
        schedulers.put("scheduled_job", scheduledJobMock);

        return schedulers;
    }
}
