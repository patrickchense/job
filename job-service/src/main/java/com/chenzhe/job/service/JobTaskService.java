package com.chenzhe.job.service;

import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;

import java.util.List;

public interface JobTaskService {

    List<JobTask>  findJobTasks(QueryJobTask queryJobTask);

    JobTask save(JobTask task);

    void updateTaskStatus(Long jobId, JobStatus aNew, JobStatus queued);

    JobTask findById(Long jobId);
}
