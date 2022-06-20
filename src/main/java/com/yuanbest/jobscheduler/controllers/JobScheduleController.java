package com.yuanbest.jobscheduler.controllers;

import com.yuanbest.jobscheduler.config.ResponseEnum;
import com.yuanbest.jobscheduler.dto.JobItem;
import com.yuanbest.jobscheduler.services.JobServices;
import com.yuanbest.jobscheduler.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@Slf4j
public class JobScheduleController {

    @Autowired
    private JobServices jobServices;

    @GetMapping("/getalljobs")
    ResponseVO getAllJobs() {
        return jobServices.getAllJobs();
    }

    @PostMapping("/addjob")
    ResponseVO createJob(@RequestBody JobItem jobItem) {
        try {
            return jobServices.createJob(jobItem);
        } catch (SchedulerException e) {
            log.error("error info is {}",e);
            return ResponseVO.Fail(ResponseEnum.CREATE_JOB_FAIL);
        }
    }

    @GetMapping("/getjob/{jobName}")
    ResponseVO getJobbyName(@PathVariable String jobName) {
        return jobServices.getJobByName(jobName);
    }

    @GetMapping("/deljob/{jobName}")
    ResponseVO DelJobbyName(@PathVariable String jobName) {
        return jobServices.delJobbyName(jobName);
    }

    @GetMapping("/pausejob/{jobName}")
    ResponseVO PauseJobbyName(@PathVariable String jobName) {
        return jobServices.pauseJob(jobName);
    }

    @GetMapping("/resumejob/{jobName}")
    ResponseVO ResumeJobbyName(@PathVariable String jobName) {
        return jobServices.ResumeJob(jobName);
    }

    @PostMapping("/updatejob")
    ResponseVO updateJobbyName(@RequestBody JobItem jobItem) {
        return jobServices.updateJob(jobItem);
    }

    @GetMapping("/pauseall")
    ResponseVO PauseAllJobs() {
        return jobServices.PauseAllJobs();
    }

    @GetMapping("/resumeall")
    ResponseVO ResumeAllJobs() {
        return jobServices.ResumeAllJobs();
    }
}
