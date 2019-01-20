package com.chenzhe.job.service;

import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTaskServiceImpl implements JobTaskService{

    @Override
    public List<JobTask> findJobTasks(QueryJobTask queryJobTask) {

        return null;
    }
}
