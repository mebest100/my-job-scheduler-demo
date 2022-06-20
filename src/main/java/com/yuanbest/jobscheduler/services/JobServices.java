package com.yuanbest.jobscheduler.services;

import com.yuanbest.jobscheduler.dto.JobItem;
import com.yuanbest.jobscheduler.vo.ResponseVO;
import org.quartz.SchedulerException;


public interface JobServices {
    ResponseVO createJob(JobItem jobItem) throws SchedulerException;

    ResponseVO getAllJobs();

    ResponseVO getJobByName(String jobName);

    ResponseVO delJobbyName(String jobName);

    //Re-schedule job or Change job desctiption
    ResponseVO updateJob(JobItem jobItem);

    ResponseVO pauseJob(String jobName);

    ResponseVO ResumeJob(String jobName);

    ResponseVO PauseAllJobs();

    ResponseVO ResumeAllJobs();

}
