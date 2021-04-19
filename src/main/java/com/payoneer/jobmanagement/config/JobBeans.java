package com.payoneer.jobmanagement.config;

import com.payoneer.jobmanagement.application.tasks.AnotherSampleTaskHandler;
import com.payoneer.jobmanagement.application.tasks.SampleTaskHandler;
import org.quartz.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JobBeans {

    @Bean
    Map<String, Job> getJobBeans() {
        Map<String, Job> beans = new HashMap();
        beans.put("SampleTaskHandler", new SampleTaskHandler());
        beans.put("AnotherSampleTaskHandler", new AnotherSampleTaskHandler());
        return beans;
    }
}
