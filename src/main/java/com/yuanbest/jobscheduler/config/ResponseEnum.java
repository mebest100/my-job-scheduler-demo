package com.yuanbest.jobscheduler.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(200,"SUCCESS"),
    SERVER_ERROR(500,"SERVER Fail"),

    JOB_NAME_REPEATED(1001,"JOB NAME REPEATED"),
    JOB_NO_EXIST(1002,"NO SUCH JOB!"),
    JOB_LIST_EMPTY(1003,"JOB LIST IS EMPTY !"),
    WRONG_OPERATION_TYPE(1004,"WRONG OPERATION TYPE!"),
    CREATE_JOB_FAIL(1005,"FAIL TO CREATE JOB!"),
    DELETE_JOB_FAIL(1006,"FAIL TO DELETE JOB!"),
    UPDATE_JOB_FAIL(1007,"FAIL TO UPDATE JOB!"),
    PAUSE_JOB_FAIL(1008,"FAIL TO PAUSE JOB!"),
    RESUME_JOB_FAIL(1009,"FAIL TO RESUME JOB!"),
    PAUSE_ALLJOBS_FAIL(1010,"FAIL TO PAUSE ALL JOBS!"),
    RESUME_ALLJOBS_FAIL(1011,"FAIL TO RESUME ALL JOBS!");

    private  Integer code;
    private  String message;
}
