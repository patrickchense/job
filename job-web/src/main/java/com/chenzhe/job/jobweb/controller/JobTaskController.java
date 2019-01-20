package com.chenzhe.job.jobweb.controller;

import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;
import com.chenzhe.job.service.JobTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobTaskController {

    @Autowired
    private JobTaskService jobTaskService;

    @PostMapping("/tasks")
    public List<JobTask> findJobTasks(@RequestBody QueryJobTask queryJobTask) {
        return jobTaskService.findJobTasks(queryJobTask);
    }
}
