package com.yuanbest.jobscheduler.services;

import com.alibaba.fastjson.JSONObject;
import com.yuanbest.jobscheduler.DBMock.DBUtils;
import com.yuanbest.jobscheduler.config.ResponseEnum;
import com.yuanbest.jobscheduler.config.RunningStatusEnum;
import com.yuanbest.jobscheduler.dto.JobItem;
import com.yuanbest.jobscheduler.jobs.JobTask;
import com.yuanbest.jobscheduler.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class JobServicesImpl implements JobServices {


    @Autowired
    private DBUtils dbUtils;

    private Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

    public JobServicesImpl() throws SchedulerException {
    }

    @Override
    public ResponseVO createJob(JobItem jobItem) {
        Trigger trigger = null;
        //create one time execution task
        if (jobItem.getInterval() == null || jobItem.getInterval() == 0) {
            trigger = TriggerBuilder.newTrigger().withIdentity(jobItem.getJobName())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .startNow()
                    .build();
        } else {
            //create repeated execution task
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobItem.getJobName())
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(jobItem.getInterval()))
                    .startNow()
                    .build();
        }

        JobDetail jobDetail = JobBuilder.newJob(JobTask.class)
                .withIdentity(jobItem.getJobName())
                .withDescription(jobItem.getJobDesc())
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start(); // must have this line,otherwise task won't start!
            log.info("Job {} has been created successfully!", jobItem.getJobName());
        } catch (SchedulerException e) {
            log.error("error info is {}", e);
            return ResponseVO.Fail(ResponseEnum.CREATE_JOB_FAIL);
        }
        return dbUtils.AddJobToFile(jobItem); //store new-added job into MockDB
    }

    @Override
    public ResponseVO getAllJobs() {
        Set<JobKey> jobKeySet = null;
        JSONObject allJobs = dbUtils.getAllJobsFromFile();

//        try {
//            //acquire numbers of jobs from quartz
//            jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
//        } catch (SchedulerException e) {
//            log.error("error info is {}",e);
//            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY) ;
//        }
        if (allJobs != null) {
            return ResponseVO.success(allJobs);
        }
        return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY);
    }

    @Override
    public ResponseVO getJobByName(String jobName) {
        return dbUtils.GetJobbyJobName(jobName);
    }

    @Override
    public ResponseVO delJobbyName(String jobName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName));
            scheduler.deleteJob(JobKey.jobKey(jobName));
            log.info("Job {} has been deleted successfully!", jobName);
        } catch (SchedulerException e) {
            log.error("deleteJob {} failed:{}", jobName, e);
            return ResponseVO.Fail(ResponseEnum.DELETE_JOB_FAIL);
        }
        return dbUtils.DelJobbyJobName(jobName); //update into MockDB after deleted specified job
    }

    @Override  //reschedule job and change job's description
    public ResponseVO updateJob(JobItem jobItem) {
        try {
            ResponseVO responseVO = dbUtils.GetJobbyJobName(jobItem.getJobName());
            if (responseVO.getCode() != ResponseEnum.SUCCESS.getCode()) {
                return ResponseVO.Fail(ResponseEnum.JOB_NO_EXIST, "CANNOT UPDATE NON-EXISTING JOB");
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(jobItem.getJobName());
            Trigger trigger = null;
            if (jobItem.getInterval() == null) { //create one time execution task
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                        .build();
            } else {
                //create repeated execution task
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(jobItem.getInterval()))
                        .build();
            }
            scheduler.rescheduleJob(triggerKey, trigger);
            scheduler.start();
            log.info("Job {} has been updated successfully!", jobItem.getJobName());
        } catch (Exception e) {
            log.error("update job {} failed:{}", jobItem.getJobName(), e);
            return ResponseVO.Fail(ResponseEnum.UPDATE_JOB_FAIL,"job "+ jobItem.getJobName() + " update failed");
        }

        return dbUtils.updateJob(jobItem);
    }

    @Override
    public ResponseVO pauseJob(String jobName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));
            log.info("Job {} has been paused successfully!", jobName);

        } catch (SchedulerException e) {
            log.error("Job {} pause failed:{}", jobName, e);
            return ResponseVO.Fail(ResponseEnum.PAUSE_JOB_FAIL);
        }
        //update into MockDB after paused specified job
        return dbUtils.updateSingleJobsbyStatus(jobName, RunningStatusEnum.PAUSED.getStatus());
    }


    @Override
    public ResponseVO ResumeJob(String jobName) {
        try {
            scheduler.resumeTrigger(TriggerKey.triggerKey(jobName));
            log.info("Job {} has been resumed successfully!", jobName);

        } catch (SchedulerException e) {
            log.error("Job {} resume failed:{}", jobName, e);
            return ResponseVO.Fail(ResponseEnum.RESUME_JOB_FAIL);
        }
        //update into MockDB after resumed specified job
        return dbUtils.updateSingleJobsbyStatus(jobName, RunningStatusEnum.RUN.getStatus());
    }


    @Override
    public ResponseVO PauseAllJobs() {
        JSONObject allJobs = dbUtils.getAllJobsFromFile();
        if (allJobs == null) {
            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY, "CANNOT PAUSE JOBS FOR EMPTY JOBLIST");
        }

        try {
            scheduler.pauseAll();
            log.info("All jobs has been paused successfully!");
        } catch (SchedulerException e) {
            log.error("PAUSE All Jobs failed:{}", e);
            return ResponseVO.Fail(ResponseEnum.PAUSE_ALLJOBS_FAIL);
        }
        //update into MockDB after resumed ALL jobs
        return dbUtils.updateAllJobsbyStatus(RunningStatusEnum.PAUSED.getStatus());
    }

    @Override
    public ResponseVO ResumeAllJobs() {
        JSONObject allJobs = dbUtils.getAllJobsFromFile();
        if (allJobs == null) {
            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY, "CANNOT RESUME JOBS FOR EMPTY JOBLIST");
        }

        try {
            scheduler.resumeAll();
            log.info("All jobs have been resumed successfully!");
        } catch (SchedulerException e) {
            log.error("RESUME All Jobs failed:{}", e);
            return ResponseVO.Fail(ResponseEnum.RESUME_ALLJOBS_FAIL);
        }
        //update into MockDB after resumed ALL jobs
        return dbUtils.updateAllJobsbyStatus(RunningStatusEnum.RUN.getStatus());
    }
}
