package com.chenzhe.job.service;

import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.context.JobQueueParam;
import com.chenzhe.job.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class JobManager {

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    private JobManagerContext jobManagerContext;
    /*
    register job
    schedule a next execution time or call it immediately

     */
    public JobExecuteContext registerJob(String name, String type, JobPriority jobPriority, Job job) throws Exception {
        JobTask task = new JobTask();
        task.setName(name);
        task.setType(type);
        task.setPriority(jobPriority);
        task.setCreateTime(System.currentTimeMillis());
        task.setStatus(JobStatus.NEW);
        task = jobTaskService.save(task);
        JobExecuteContext context = new JobExecuteContext();
        context.setTask(task);
        context.setJob(job);
        jobManagerContext.addJobExecuteContext(task.getId(), context);
        return context;
    }

    /*
    check schedule type
    put job into queue
     */
    public void startJob(Long jobId, long nextRunTime, long period) throws Exception {
        if (!jobManagerContext.containsJobExecuteContext(jobId)) {
            throw new Exception("task not exist");
        }
        JobScheduler scheduler = new JobScheduler();
        scheduler.setJobTaskId(jobId);
        scheduler.setNextExecuteTime(nextRunTime);
        long now = System.currentTimeMillis();
        if (nextRunTime <= now) {
            scheduler.setSchedulerType(SchedulerType.RIGHTNOW);
        } else if (period < 0) {
            scheduler.setSchedulerType(SchedulerType.ONCE);
        } else {
            scheduler.setSchedulerType(SchedulerType.SCHEDULED);
            scheduler.setPeriod(period);
        }
        JobExecuteContext context = jobManagerContext.getJobExecuteContext(jobId);
        context.setScheduler(scheduler);
        JobQueueParam param = new JobQueueParam();
        param.setJobTaskId(jobId);
        param.setNextExecuteTime(context.getScheduler().getNextExecuteTime());
        param.setPriority(context.getTask().getPriority());
        jobManagerContext.addJobToQueue(param);
        jobTaskService.updateTaskStatus(jobId, JobStatus.NEW, JobStatus.QUEUED);
        context.setTask(jobTaskService.findById(jobId));
    }

    /*
        rightnow & once job should run directly
        scheduled job should put into queue wait for manager to run the job
     */
    @Transactional
    public boolean execute(Long jobId) {
        if (jobManagerContext.containsJobExecuteContext(jobId)) {
            // running the job task
            JobExecuteContext context = jobManagerContext.getJobExecuteContext(jobId);
            jobTaskService.updateTaskStatus(jobId, context.getTask().getStatus(), JobStatus.RUNNING);
            try {
                context.getJob().execute();
            } catch (Exception e) {
                log.error(String.format("[job.manager]execute job failed, jobId:%d", jobId), e);
                jobTaskService.updateTaskStatus(jobId, JobStatus.RUNNING, JobStatus.FAILED);
                context.setTask(jobTaskService.findById(jobId));
                return false;
            }
            // success
            jobTaskService.updateTaskStatus(jobId, JobStatus.RUNNING, JobStatus.SUCCESS);
            context.setTask(jobTaskService.findById(jobId));
            return true;
        }
        log.warn(String.format("[job.manager]execute job not exist, jobId:%d", jobId));
        return false;
    }

    /*
        recreate a job task and put into map and queue
     */
    public void reScheduleJob(Long jobTaskId) throws Exception {
        if (jobManagerContext.containsJobExecuteContext(jobTaskId)) {
            JobExecuteContext context = jobManagerContext.getJobExecuteContext(jobTaskId);
            if (context.getScheduler().getSchedulerType() == SchedulerType.SCHEDULED) {
                try {
                    JobExecuteContext newContext = registerJob(context.getTask().getName(), context.getTask().getType(), context.getTask().getPriority(), context.getJob());
                    startJob(newContext.getTask().getId(), context.getScheduler().getNextExecuteTime() + context.getScheduler().getPeriod(), context.getScheduler().getPeriod());
                } catch (Exception e) {
                    log.error(String.format("[job.manager]reschedule job failed, jobId:%d", jobTaskId), e);
                }
            }
            else {
                throw new Exception("only support scheduled type job task to reschedule");
            }
        }
    }
}
