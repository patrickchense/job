package com.chenzhe.job.service;

import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;

import java.util.List;

public interface JobTaskService {

    List<JobTask>  findJobTasks(QueryJobTask queryJobTask);
}
