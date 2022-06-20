package com.yuanbest.jobscheduler.vo;

import com.yuanbest.jobscheduler.config.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO {
    private long code;
    private String message;
    private Object data;

    public static ResponseVO success() {
        return new ResponseVO(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), null);
    }

    public static ResponseVO success(Object data) {
        return new ResponseVO(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), data);
    }

    public static ResponseVO Fail(ResponseEnum FailEnum) {
        return new ResponseVO(FailEnum.getCode(), FailEnum.getMessage(), null);
    }

    public static ResponseVO Fail(ResponseEnum FailEnum,Object data) {
        return new ResponseVO(FailEnum.getCode(), FailEnum.getMessage(), data);
    }

}
