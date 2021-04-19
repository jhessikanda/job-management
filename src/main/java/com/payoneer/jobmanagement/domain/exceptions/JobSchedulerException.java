package com.payoneer.jobmanagement.domain.exceptions;

public class JobSchedulerException extends Exception {

    public JobSchedulerException(String message) {
        super(message);
    }

    public JobSchedulerException(String message, Throwable cause) {
        super(message, cause);
    }
}
