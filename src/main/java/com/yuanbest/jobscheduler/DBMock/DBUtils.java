package com.yuanbest.jobscheduler.DBMock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yuanbest.jobscheduler.config.ResponseEnum;
import com.yuanbest.jobscheduler.config.RunningStatusEnum;
import com.yuanbest.jobscheduler.dto.JobItem;
import com.yuanbest.jobscheduler.vo.JobVO;
import com.yuanbest.jobscheduler.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class DBUtils {


    private String path = "F:\\tmp\\jsonData.json";

    static void WriteJson(String path, JSONObject jsonObject) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(path));

            String pretty = JSON.toJSONString(jsonObject,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);


            bw.write(pretty);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void TestAddJobToFile() {
        JobItem jobItem = new JobItem("myJob2", "job2 Executed", 20);
        AddJobToFile(jobItem);
    }


    public ResponseVO AddJobToFile(JobItem jobItem) {
        File FilePath = new File(path);
        JobVO jobVO = new JobVO();
        BeanUtils.copyProperties(jobItem, jobVO);
        jobVO.setStatus(RunningStatusEnum.RUN.getStatus());

        String jsonString = JSON.toJSONString(jobVO);


        JSONObject jsonObject = null;

        try {
            if (!FilePath.exists()) {
                jsonObject = new JSONObject();
                jsonObject.put(jobVO.getJobName(), jsonString);

            } else {
                String fileContent = FileUtils.readFileToString(FilePath);
                jsonObject = JSON.parseObject(fileContent, JSONObject.class);
                if (jsonObject == null) {
                    System.out.println("json File is Empty£¡");
                    jsonObject = new JSONObject();
                    jsonObject.put(jobVO.getJobName(), jsonString);
                    WriteJson(path, jsonObject);
                    return ResponseVO.success(String.format("Add job %s ok!", jobVO.getJobName()));
                }
                if (jsonObject.getString(jobVO.getJobName()) != null) {
                    log.error("jobName Repeated");
                    return ResponseVO.Fail(ResponseEnum.JOB_NAME_REPEATED);
                }

                jsonObject.put(jobVO.getJobName(), jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        WriteJson(path, jsonObject);

        return ResponseVO.success(String.format("Add %s ok!", jobVO.getJobName()));

    }

    public JSONObject getAllJobsFromFile() {
        File FilePath = new File(path);
        try {
            String fileContent = FileUtils.readFileToString(FilePath);
            if (StringUtils.isEmpty(fileContent)) {
                return null;
            }

            JSONObject data = JSON.parseObject(fileContent, JSONObject.class);
            if (data.entrySet().isEmpty()) { //in case empty object but dual brackets exist
                return null;
            }
            return data;
        } catch (IOException e) {
            log.error("error info is {}", e);
            return null;
        }
    }

    @Test
    public void TestDelJobbyJobName() {
        String jbName = "myJob4";
        System.out.println(DelJobbyJobName(jbName));
    }

    public ResponseVO DelJobbyJobName(String jobName) {
        JSONObject alljobs = getAllJobsFromFile();
        if (alljobs == null) {
            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY, "CANNOT DELETE SPECIFIED JOB!");
        }

        if (alljobs.getString(jobName) != null
        ) {
            alljobs.remove(jobName);
            WriteJson(path, alljobs);
            return ResponseVO.success(jobName + " deleted");
        } else {
            return ResponseVO.Fail(ResponseEnum.JOB_NO_EXIST);
        }
    }

    public ResponseVO GetJobbyJobName(String jobName) {
        JSONObject alljobs = getAllJobsFromFile();
        if (alljobs == null) {
            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY);
        }
        if (alljobs.getString(jobName) != null) {
            return ResponseVO.success(alljobs.getString(jobName));
        } else {
            return ResponseVO.Fail(ResponseEnum.JOB_NO_EXIST);
        }
    }

    @Test
    public void TestUpdateJob() {
        JobItem jobItem = new JobItem("myJob2", "job2 updated again8", 20);
        updateJob(jobItem);
    }

    public ResponseVO updateJob(JobItem jobItem) {
        JSONObject allJobs = getAllJobsFromFile();
        if (allJobs == null) {
            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY, "CANNOT UPDATE JOB");
        }

        String jobName = jobItem.getJobName();

        if (allJobs.getString(jobName) != null
        ) {
            JobVO jobVO = JSON.parseObject(allJobs.getString(jobName), JobVO.class);
            BeanUtils.copyProperties(jobItem, jobVO);
            allJobs.put(jobName, JSON.toJSONString(jobVO));
            WriteJson(path, allJobs);
            return ResponseVO.success(String.format("update %s ok!", jobName));

        } else {
            return ResponseVO.Fail(ResponseEnum.JOB_NO_EXIST, "CANNOT UPDATE NOT-EXISTING JOB");
        }
    }

    @Test
    public void TestupdateAllJobsbyStatus() {
        updateAllJobsbyStatus(RunningStatusEnum.STOPPED.getStatus());
    }

    public ResponseVO updateAllJobsbyStatus(String status) {
        JSONObject allJobs = getAllJobsFromFile();
        if (allJobs == null) {
            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY, "CANNOT UPDATE JOB");
        }
        for (Map.Entry<String, Object> jobMap : allJobs.entrySet()) {
            String jobName = jobMap.getKey();
            JobVO jobObject = JSON.parseObject(jobMap.getValue().toString(), JobVO.class);
            jobObject.setStatus(status);
            allJobs.put(jobName, JSON.toJSONString(jobObject));
        }
        WriteJson(path, allJobs);
        return ResponseVO.success("All Jobs set to " + status);
    }

    @Test
    public void TestUpdateSingleJobsbyStatus() {
        updateSingleJobsbyStatus("myJob2", RunningStatusEnum.RUN.getStatus());
    }

    public ResponseVO updateSingleJobsbyStatus(String jobName, String status) {
        JSONObject allJobs = getAllJobsFromFile();
        if (allJobs == null) {
            return ResponseVO.Fail(ResponseEnum.JOB_LIST_EMPTY, "CANNOT UPDATE JOB");
        }

        if (allJobs.getString(jobName) != null
        ) {
            JobVO jobVO = JSON.parseObject(allJobs.getString(jobName), JobVO.class);
            jobVO.setStatus(status);
            allJobs.put(jobName, JSON.toJSONString(jobVO));
            WriteJson(path, allJobs);
            return ResponseVO.success(String.format("%s set to %s!", jobName, status));

        } else {
            return ResponseVO.Fail(ResponseEnum.JOB_NO_EXIST, "CANNOT UPDATE NOT-EXISTING JOB");
        }

    }


}


