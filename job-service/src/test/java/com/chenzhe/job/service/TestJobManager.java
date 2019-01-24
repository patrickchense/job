package com.chenzhe.job.service;

import com.chenzhe.job.JobApplication;
import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.entity.JobPriority;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = JobApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJobManager {

    @Autowired
    private JobManager jobManager;

    @Autowired
    private JobManagerContext jobManagerContext;

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    private JobExecuteThread jobExecuteThread;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        jobExecuteThread.turnOffAuto();
        jobManagerContext.clear();
    }

    @After
    public void clear() {
        jobManagerContext.clear();
    }

    @Test
    public void testRegisterJob() throws Exception {
        JobExecuteContext context = jobManager.registerJob("testJobManager1", "type1", JobPriority.LOW, new Job() {
            @Override
            public void execute() throws Exception {
                System.out.println("test register job");
            }
        });
        Assert.assertEquals(1, jobManagerContext.getJOBS().size());
        Assert.assertEquals("type1", context.getTask().getType());
    }

    @Test
    public void testStartJobNotExist() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("task not exist");
        jobManager.startJob(-20L, 100, -1);
    }

    @Test
    public void testStartJob() throws Exception {
        JobExecuteContext context = jobManager.registerJob("testJobManager2", "type1", JobPriority.LOW, new Job() {
            @Override
            public void execute() throws Exception {
                System.out.println("test register job");
            }
        });
        long now = System.currentTimeMillis();
        long nowPlus100Seconds = now + TimeUnit.SECONDS.toMillis(100);

        jobManager.startJob(context.getTask().getId(), nowPlus100Seconds, -1);
        JobExecuteContext context1 = jobManagerContext.getJobExecuteContext(context.getTask().getId());
        Assert.assertEquals(nowPlus100Seconds, context1.getScheduler().getNextExecuteTime().longValue());
        Assert.assertNotNull(jobManagerContext.peekJobParam());
        Assert.assertEquals(nowPlus100Seconds, jobManagerContext.peekJobParam().getNextExecuteTime().longValue());
        JobTask jobTask = jobTaskService.findById(context.getTask().getId());
        Assert.assertEquals(JobStatus.QUEUED, jobTask.getStatus());
    }

    @Test
    public void testExecute() throws Exception {
        JobExecuteContext context = jobManager.registerJob("testJobManager3", "type1", JobPriority.LOW, new Job() {
            @Override
            public void execute() throws Exception {
                System.out.println("test register job");
            }
        });
        long now = System.currentTimeMillis();
        long nowPlus100Seconds = now + TimeUnit.SECONDS.toMillis(100);

        jobManager.startJob(context.getTask().getId(), nowPlus100Seconds, -1);
        jobManager.execute(context.getTask().getId());
        JobTask jobTask = jobTaskService.findById(context.getTask().getId());
        Assert.assertEquals(JobStatus.SUCCESS, jobTask.getStatus());
    }

    @Test
    public void testExecuteNotExist() {
        Assert.assertFalse(jobManager.execute(-20L));
    }

    @Test
    public void testExecuteFailed() throws Exception {
        JobExecuteContext context = jobManager.registerJob("testJobManager4", "type1", JobPriority.LOW, new Job() {
            @Override
            public void execute() throws Exception {
                throw new RuntimeException("test failed");
            }
        });
        long now = System.currentTimeMillis();
        long nowPlus100Seconds = now + TimeUnit.SECONDS.toMillis(100);

        jobManager.startJob(context.getTask().getId(), nowPlus100Seconds, -1);
        jobManager.execute(context.getTask().getId());
        JobTask jobTask = jobTaskService.findById(context.getTask().getId());
        Assert.assertEquals(JobStatus.FAILED, jobTask.getStatus());
    }

    @Test
    public void testReScheduleJob() throws Exception {
        JobExecuteContext context = jobManager.registerJob("testJobManager5", "type1", JobPriority.LOW, new Job() {
            @Override
            public void execute() throws Exception {
                System.out.println("test register job");
            }
        });
        long now = System.currentTimeMillis();
        long nowPlus100Seconds = now + TimeUnit.SECONDS.toMillis(100);

        jobManager.startJob(context.getTask().getId(), nowPlus100Seconds, TimeUnit.SECONDS.toMillis(100));
        jobManager.execute(context.getTask().getId());
        JobTask jobTask = jobTaskService.findById(context.getTask().getId());
        Assert.assertEquals(JobStatus.SUCCESS, jobTask.getStatus());
        jobManager.reScheduleJob(context.getTask().getId());
        QueryJobTask queryJobTask = new QueryJobTask();
        queryJobTask.setName("testJobManager5");
        List<JobTask> tasks = jobTaskService.findJobTasks(queryJobTask);
        Assert.assertEquals(2, tasks.size());
        tasks.sort((a, b) -> a.getCreateTime() < b.getCreateTime() ? 1 : 0);
        Assert.assertEquals(JobStatus.QUEUED, tasks.get(1).getStatus());
    }
}
