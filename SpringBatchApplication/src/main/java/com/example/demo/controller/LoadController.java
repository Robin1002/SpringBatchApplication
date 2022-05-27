package com.example.demo.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/load")
public class LoadController {

    @Autowired
    JobLauncher jobLauncher; //created by SB itself

    @Autowired
    Job job; // created in SpringBatchConfig.java class

    // We are going to load the Job, in order to load the Job we need to trigger the Job Launcher,So Autowire Job Launcher 
    // whn u trigger the JobLauncher u need to provide the job as well so the Autowire Job
    @GetMapping
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, Exception {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters); 

        System.out.println("Batch is Running...");
        while (jobExecution.isRunning()) {
            System.out.println("...");
        }
        
        System.out.println("JobExecution: " + jobExecution.getStatus());
       /* StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        System.out.println("ReadCount = " + stepExecution.getReadCount());
        System.out.println("ReadSkipCount = " + stepExecution.getReadSkipCount());
        System.out.println("WriteCount = " + stepExecution.getWriteCount());
        System.out.println("WriteSkipCount = " + stepExecution.getWriteSkipCount());
        System.out.println("ProcessSkipCount = " + stepExecution.getProcessSkipCount());
        System.out.println("CommitCount = " + stepExecution.getCommitCount());
        System.out.println("RollbackCount = " + stepExecution.getRollbackCount());
        System.out.println("FailureExceptions = " + stepExecution.getFailureExceptions());
        System.out.println("JobStartTime: " + jobExecution.getStartTime()+"---end time---"+jobExecution.getEndTime()+"---diff--"+(jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime()));
        */
        System.out.println("Batch process completed...");
        return jobExecution.getStatus();
    }
}
