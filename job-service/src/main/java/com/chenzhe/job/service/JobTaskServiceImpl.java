package com.chenzhe.job.service;

import com.chenzhe.job.dao.JobSchedulerRepository;
import com.chenzhe.job.dao.JobTaskRepository;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTaskServiceImpl implements JobTaskService{

    @Autowired
    private JobTaskRepository jobTaskRepository;

    @Autowired
    private JobSchedulerRepository jobSchedulerRepository;

    @Override
    public List<JobTask> findJobTasks(QueryJobTask queryJobTask) {

        return null;
    }

    @Override
    public JobTask save(JobTask task) {
       return jobTaskRepository.save(task);
    }

    @Override
    public void updateTaskStatus(Long jobId, JobStatus oldStatus, JobStatus newStatus) {
        jobTaskRepository.updateStatus(jobId, oldStatus, newStatus);
    }

    @Override
    public JobTask findById(Long jobId) {
        return jobTaskRepository.findById(jobId).get();
    }

}
