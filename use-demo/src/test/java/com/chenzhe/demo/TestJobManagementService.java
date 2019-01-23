package com.chenzhe.demo;

import com.chenzhe.job.JobApplication;
import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.entity.JobPriority;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.service.Job;
import com.chenzhe.job.service.JobManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = JobApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJobManagementService {

    @Autowired
    JobManager jobManager;

    @Test
    public void testSingleJob() throws Exception {
        JobExecuteContext context = jobManager.registerJob("testDemo1", "demo1", JobPriority.LOW, new Job() {
            @Override
            public void execute() throws Exception {
                System.out.println("test Demo 1");
            }
        });
        jobManager.startJob(context.getTask().getId(), System.currentTimeMillis(), -1); //right now
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        JobTask jobTask = jobManager.getJobTaskService().findById(context.getTask().getId());
        Assert.assertEquals(JobStatus.SUCCESS, jobTask.getStatus());
    }

    private static boolean flag = false;

    @Test
    public void testPriorityJob() throws Exception {
        JobExecuteContext context = jobManager.registerJob("testDemo1", "demo1", JobPriority.LOW, new Job() {
            @Override
            public void execute() throws Exception {
                flag = false;
                System.out.println("test Demo 1");
            }
        });
        long now = System.currentTimeMillis();
        jobManager.startJob(context.getTask().getId(), now + TimeUnit.SECONDS.toMillis(5), -1); //right now
        JobExecuteContext context1 = jobManager.registerJob("testDemo2", "demo2", JobPriority.HIGH, new Job() {
            @Override
            public void execute() throws Exception {
                flag = true;
                System.out.println("test Demo 2");
            }
        });
        jobManager.startJob(context1.getTask().getId(), now + TimeUnit.SECONDS.toMillis(5), -1); //right now
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        // both finished and success
        JobTask jobTask = jobManager.getJobTaskService().findById(context.getTask().getId());
        Assert.assertEquals(JobStatus.SUCCESS, jobTask.getStatus());
        JobTask jobTask1 = jobManager.getJobTaskService().findById(context1.getTask().getId());
        Assert.assertEquals(JobStatus.SUCCESS, jobTask1.getStatus());
        // check priority, though output sequence and can check flag also， here use the JobPriority to check, because next execution time is the same.
        // flag should be false,  false -> true -> false
        Assert.assertFalse(flag);
    }
}
