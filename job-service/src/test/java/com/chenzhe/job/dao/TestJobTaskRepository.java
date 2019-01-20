package com.chenzhe.job.dao;

import com.chenzhe.job.entity.JobPriority;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestJobTaskRepository {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JobTaskRepository jobTaskRepository;

    @Test
    public void testSave() {
        JobTask jobTask = new JobTask();
        jobTask.setName("test1");
        jobTask.setPriority(JobPriority.MID);
        jobTask.setStatus(JobStatus.QUEUED);
        jobTask.setCreateTime(System.currentTimeMillis());
        jobTask = jobTaskRepository.save(jobTask);
        Assert.assertNotNull(jobTask.getId());
        JobTask jobTask1 = jobTaskRepository.findById(jobTask.getId()).get();
        Assert.assertEquals(jobTask.getPriority(), jobTask1.getPriority());
        Assert.assertEquals(jobTask.getStatus(), jobTask1.getStatus());
    }
}
