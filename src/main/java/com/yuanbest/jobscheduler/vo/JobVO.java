package com.yuanbest.jobscheduler.vo;

import com.yuanbest.jobscheduler.dto.JobItem;
import lombok.Data;

@Data
public class JobVO extends JobItem {
    String status;
}
