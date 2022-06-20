package com.yuanbest.jobscheduler.controllers;


import com.yuanbest.jobscheduler.DBMock.DBUtils;
import com.yuanbest.jobscheduler.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class testController {

    @Autowired  //这里必须加autowired的注解，否则会抛出空指针错误
    private DBUtils DBUtils;

    @GetMapping("/getAlljobs")
    ResponseVO getJobs() {
        return ResponseVO.success(DBUtils.getAllJobsFromFile());
    }

    @GetMapping("/getjob/{jbname}")
    ResponseVO queryJob(@PathVariable  String jbname) {
        return DBUtils.GetJobbyJobName(jbname);
    }
}
