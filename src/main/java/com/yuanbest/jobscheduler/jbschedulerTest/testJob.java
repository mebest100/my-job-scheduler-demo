package com.yuanbest.jobscheduler.jbschedulerTest;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class testJob {
    public static void main(String[] args) throws SchedulerException, ClassNotFoundException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail1 = createJob("myJob","job1","group1", "yuanbest");

        JobDetail jobDetail2 = createJob("myJob","job2","group1", "mebest");

        SimpleTrigger trigger1 = createTrigger("trigger1", "group1");
        SimpleTrigger trigger2 = createTrigger("trigger2", "group1");

        scheduler.scheduleJob(jobDetail1, trigger1);
        scheduler.scheduleJob(jobDetail2, trigger2);
        scheduler.start();
    }

    static  JobDetail createJob(String jobClassName, String jobName,String jobGroup
                                ,String jobUser) throws ClassNotFoundException {
        Class< ? extends Job > jobClass = (Class<? extends Job>) Class.forName("com.yuanbest.jobscheduler.jobs."+ jobClassName);
       return JobBuilder.newJob(jobClass)
               .withIdentity(jobName, jobGroup)
               .usingJobData("jobInfo", jobUser+ "'s job executed")
               .usingJobData("user",jobUser)
               .build();
    }

    static SimpleTrigger createTrigger(String triggerName,String triggerGroup) {
        return   TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1))
                .build();
    }
}
