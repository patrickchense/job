package com.chenzhe.job.service;

import com.chenzhe.job.JobApplication;
import com.chenzhe.job.entity.JobPriority;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = JobApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJobTaskService {

    @Autowired
    private JobTaskService jobTaskService;


    @Test
    public void testFindJobTasks() {
        JobTask jobTask = new JobTask();
        jobTask.setName("test1");
        jobTask.setPriority(JobPriority.MID);
        jobTask.setStatus(JobStatus.QUEUED);
        jobTask.setCreateTime(System.currentTimeMillis());
        jobTask = jobTaskService.save(jobTask);

        QueryJobTask queryJobTask = new QueryJobTask();
        queryJobTask.setName("test1");
        List<JobTask> jobTasks = jobTaskService.findJobTasks(queryJobTask);
        Assert.assertEquals(1, jobTasks.size());
        Assert.assertEquals(JobPriority.MID, jobTasks.get(0).getPriority());

        QueryJobTask queryJobTask1 = new QueryJobTask();
        queryJobTask1.setCreateTimeStart(System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(5));
        List<JobTask> jobTasks1 = jobTaskService.findJobTasks(queryJobTask);
        Assert.assertEquals(1, jobTasks1.size());
        Assert.assertEquals(JobPriority.MID, jobTasks1.get(0).getPriority());

        QueryJobTask queryJobTask2 = new QueryJobTask();
        queryJobTask2.setCreateTimeEnd(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
        List<JobTask> jobTasks2 = jobTaskService.findJobTasks(queryJobTask);
        Assert.assertEquals(1, jobTasks2.size());
        Assert.assertEquals(JobPriority.MID, jobTasks2.get(0).getPriority());
    }
}
