package com.payoneer.jobmanagement.domain.model;

import static com.payoneer.jobmanagement.domain.model.JobStatus.QUEUED;

public class Job {
    private Long id;
    private String description;
    private String handler;
    private String scheduler;
    private Integer priority;
    private JobStatus status = QUEUED;
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void failed() {
        this.status = JobStatus.FAILED;
    }

    public void succeed() {
        this.status = JobStatus.SUCCESS;
    }

    public void running() {
        this.status = JobStatus.RUNNING;
    }
}
