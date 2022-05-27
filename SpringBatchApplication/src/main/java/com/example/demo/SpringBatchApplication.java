package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;

@EnableRetry
@EnableScheduling
@SpringBootApplication
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}
	
	@Autowired
    JobLauncher jobLauncher; //created by SB itself

    @Autowired
    Job job; // created in SpringBatchConfig.java class

    // We are going to load the Job, in order to load the Job we need to trigger the Job Launcher,So Autowire Job Launcher 
    // whn u trigger the JobLauncher u need to provide the job as well so the Autowire Job
    //30 54 3 26 4 2 -> “At 03:54:30 on day-of-month 26 and on Tuesday in April.”
    @GetMapping
    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(3000))
    @Scheduled(cron="00 26 15 * * *") // sec min hr day_of_mnth(i.e date) mnth day_of_week
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, Exception {
    	
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters); 

        System.out.println("JobExecution: " + jobExecution.getStatus());

        System.out.println("Batch is Running...");
        while (jobExecution.isRunning()) {
            System.out.println("...");
        }
       // if(1 != 0)
        //	throw new RuntimeException();
        System.out.println("Batch process completed...");
        return jobExecution.getStatus();
    }

    @Recover 
    public BatchStatus recover(RuntimeException exception) { 
    System.out.println("--------------------Recover called--in main cls-------------------------------"+exception.getMessage());
    return null; }

}
