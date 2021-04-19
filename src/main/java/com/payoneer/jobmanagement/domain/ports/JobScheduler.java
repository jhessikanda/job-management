package com.payoneer.jobmanagement.domain.ports;

import com.payoneer.jobmanagement.domain.exceptions.JobSchedulerException;
import com.payoneer.jobmanagement.domain.model.Job;

public interface JobScheduler {
    void execute(Job job) throws JobSchedulerException;
}
