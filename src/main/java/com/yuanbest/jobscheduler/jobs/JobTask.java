package com.yuanbest.jobscheduler.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class JobTask implements Job {
    @Override
    public void execute(JobExecutionContext Context) throws JobExecutionException {
        String jobName = Context.getJobDetail().getKey().getName();
        System.out.println(String.format("job %s executed!",jobName));
        log.info("job {}  executed!",jobName);
    }
}
