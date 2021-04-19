package com.payoneer.jobmanagement.application;

import com.payoneer.jobmanagement.domain.model.Job;
import com.payoneer.jobmanagement.domain.ports.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JobRunnerController {

    private static Logger LOGGER = LoggerFactory.getLogger(JobRunnerController.class);

    private final Map<String, JobScheduler> schedulers;

    public JobRunnerController(Map<String, JobScheduler> schedulers) {
        this.schedulers = schedulers;
    }

    public Collection<Job> execute(Collection<Job> jobs) {
        List<Job> result = new ArrayList<>();

        try {
            LOGGER.info("Started to process jobs ... ");
            LOGGER.info("Total number  of jobs {} ", jobs.size());

            // Scheduled jobs
             jobs.stream()
                    .filter(job -> job.getScheduler() != null && !job.getScheduler().isEmpty())
                    .filter(job -> job.isEnabled())
                    .collect(Collectors.toList())
                    .forEach(job -> {

                        try {
                            JobScheduler scheduler = getScheduler("scheduled_job");
                            scheduler.execute(job);
                        } catch (Exception e) {
                            job.failed();
                            result.add(job);

                            LOGGER.error("Error occurred when trying to schedule job {}. Details: {} ",
                                    job.getHandler(), e.getMessage());
                            return;
                        }
                        result.add(job);

                    });

            // Immediate runnable jobs
            jobs.stream()
                    .filter(job -> job.getScheduler() == null || job.getScheduler().isEmpty())
                    .filter(job -> job.isEnabled())
                    .collect(Collectors.toList())
                    .forEach(job -> {
                        try {
                            JobScheduler scheduler = getScheduler("immediate_execution_job");
                            scheduler.execute(job);
                        } catch(Exception e) {
                            job.failed();
                            result.add(job);

                            LOGGER.error("Error occurred when trying to run job {}. Details: {} ",
                                    job.getHandler(), e.getMessage());
                            return;
                        }

                        job.succeed();
                        result.add(job);
                    });

        } catch (Exception e) {
            LOGGER.error("Error occurred while executing Job Runner. Details: {} ", e.getMessage());
        }

        return Collections.unmodifiableList(result);
    }

    private JobScheduler getScheduler(String name) throws IllegalArgumentException {
        JobScheduler scheduler = this.schedulers.get(name);

        if(scheduler == null) {
            throw new IllegalArgumentException("Invalid job scheduler");
        }

        return scheduler;
    }
}
