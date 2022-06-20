package com.yuanbest.jobscheduler.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RunningStatusEnum {
    RUN("running"),
    PAUSED("paused"),
    STOPPED("stopped");

    private String status;

}
