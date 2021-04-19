# JOB MANAGEMENT SYSTEM

This is a simplified Java version of a job management system designed for a code challenge.
It is possible to schedule a job or run it immediately.

For the purpose of this project I used the following libraries and frameworks:
- Java 8
- Spring Boot (dependency injection)
- Quartz (trigger and schedule jobs)
- Junit and Mockito (unit tests)
- Slf4j (logging)
- Gradle (manage dependencies)

## Architecture

I followed the hexagonal(ports & adapters) principles to develop this project.
The idea is to isolate the application from external tool as much as possible, leaving the core(business logic) unaware and not dependable of technology/vendor.

This is the folder structure:

```
.src/main
|--java
|    |---com.payoneer.jobmanagement
|    |      |--adapters
|    |      |      |--scheduler
|    |      |--application
|    |      |      |--tasks
|    |      |--config
|    |      |--domain
|    |      |      |--exceptions
|    |      |      |--model
|    |      |      |--ports
|    |------- Application.java
|--resources

```

## Steps to configure a new job to be run in the system:

1)  In *application.yml* file define your job configuration:
```yml
--
      id: 1
      description: Sample scheduled task job
      handler: SampleTaskHandler
      scheduler: "0 * * * * ?"
      priority: 0
      enabled: true
--
      id: 2
      description: Another sample task job
      handler: AnotherSampleTaskHandler
      enabled: true
```

* If no scheduler expression is assigned, the system will assume it should run the job immediately.
* For scheduled jobs use CRON expressions as defined in the [documentation](https://www.javadoc.io/doc/org.quartz-scheduler/quartz/latest/org/quartz/CronTrigger.html).

2) Define your Job handler class inside *[job -> handlers]* package. This class is responsible to execute your specific job action.
Two samples were created to demonstrate this behaviour.
3) Once your handler is defined, add it to the JobBean map. This data structure is responsible to map the handler name to its class.
Note: I tried to use Reflection here, but it wasn't possible.
4) Run *Applicaton.java*

## Run Tests

To run tests use following script:

```
    ./gradlew test
```

## Potential Improvements:
- We could use a persistent layer to store job execution data for future reports and monitoring.
- Provide a manual execution for eventual jobs, and not only when they registered in the system.
- If we need to update a job configuration, a new deploy is needed. Think of a way to make this more flexible.
- Increase test coverage.

##### Total of hours spent: 8 hours