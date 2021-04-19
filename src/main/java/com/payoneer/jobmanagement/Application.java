package com.payoneer.jobmanagement;

import com.payoneer.jobmanagement.config.JobConfig;
import com.payoneer.jobmanagement.application.JobRunnerController;
import com.payoneer.jobmanagement.domain.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    private JobConfig jobConfig;

    @Autowired
    private JobRunnerController jobRunnerController;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run();
    }

    @Override
    public void run(String... args) {
        Collection<Job> result = jobRunnerController.execute(jobConfig.getJobs());

        result.forEach(job -> LOGGER.info("Job: {} => Status: {} ", job.getHandler(), job.getStatus()));
    }
}
