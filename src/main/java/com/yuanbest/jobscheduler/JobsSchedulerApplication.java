package com.yuanbest.jobscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.yuanbest.jobscheduler")
public class JobsSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobsSchedulerApplication.class, args);
    }

}
