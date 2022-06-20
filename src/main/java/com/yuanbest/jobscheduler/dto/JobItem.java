package com.yuanbest.jobscheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobItem implements Serializable {

    private static final long serialVersionUID = 5559533164462122941L;
    String jobName;

    String jobDesc;

    Integer interval;
}
