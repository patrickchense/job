package com.chenzhe.job.service;

import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.context.JobQueueParam;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.SchedulerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.chenzhe.job.service.JobManager.*;

/*
runs continuous to find out the next execute job.

 */
@Service
@Slf4j
public class JobExecuteThread extends Thread {

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    private JobManager jobManager;

    /*
    pause time default 1000ms
     */
    @Value("${job.execute.pause:5000")
    private long pauseTime;

    @Override
    public void run() {
        while(true) {
            JobQueueParam param = JOB_EXECUTE_QUEUE.peek();
            if (param.getNextExecuteTime() <= System.currentTimeMillis()) {
                JOB_EXECUTE_QUEUE.poll();
                JobExecuteContext context = JOBS.get(param.getJobTaskId());
                if (JobStatus.QUEUED != context.getTask().getStatus()) {
                    log.warn(String.format("[JOB_EXECUTOR] execute job[%d] status wrong in cache", param.getJobTaskId()));
                    continue;
                }
                boolean result = jobManager.execute(param.getJobTaskId());
                // handle result
                // find next run time
                if (context.getScheduler().getSchedulerType() == SchedulerType.SCHEDULED) {
                    // create another JobExecuteContext
                    jobManager.reScheduleJob(param.getJobTaskId());
                    try {
                        jobManager.removeJob(param.getJobTaskId());
                    } catch (Exception e) {
                        log.error("[JOB_EXECUTOR] remove finished job from cache failed", e);
                    }
                }
            } else {
                try {
                    Thread.sleep(pauseTime);
                } catch (InterruptedException e) {
                    log.error("[JOB_EXECUTOR] timer failed", e);
                }
            }
        }
    }
}
