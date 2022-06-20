package com.yuanbest.jobscheduler.jobs;

import lombok.Data;
import org.quartz.*;

import java.text.SimpleDateFormat;

@Data
public class myJob implements Job {
    //这个job实现类不能有构造方法，否则execute方法不会执行，执行结果就是一片空
    @Override
    public void execute(JobExecutionContext Context) throws JobExecutionException {
        JobDataMap jobDataMap = Context.getJobDetail().getJobDataMap();
        String jobName = Context.getJobDetail().getKey().getName();

        System.out.println(String.format("time is %s === %s === data is %s",
                getTime(), jobDataMap.get("jobInfo"), jobDataMap.get("user")));
        System.out.println("JobName is " + jobName);

    }

    static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss:sss");
        return dateFormat.format(System.currentTimeMillis());
    }

    public static void main(String[] args) {
        System.out.println(myJob.class);
    }

}
