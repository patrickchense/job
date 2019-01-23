package com.chenzhe.job.service;

import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.context.JobQueueParam;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.SchedulerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


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

    @Autowired
    private JobManagerContext jobManagerContext;

    /*
    pause time default 1000ms
     */
    @Value("${job.execute.pause:10000}")
    private Long pauseTime;

    @Value("${job.execute.switch:true}")
    private Boolean trigger;

    @Override
    public void run() {
        if (log.isInfoEnabled()) {
            log.info("[JOB_EXECUTOR] thread starts watching");
        }
        while(trigger) {
            while (jobManagerContext.queueEmpty()) {
                try {
                    Thread.sleep(pauseTime);
                } catch (InterruptedException e) {
                    log.error("[JOB_EXECUTOR] timer failed", e);
                    break;
                }
            }
            JobQueueParam param = jobManagerContext.peekJobParam();
            log.info("[JOB_EXECUTOR] param:" + param);
            if (param.getNextExecuteTime() <= System.currentTimeMillis()) {
                jobManagerContext.pollJobParam();
                JobExecuteContext context = jobManagerContext.getJobExecuteContext(param.getJobTaskId());
                if (JobStatus.QUEUED != context.getTask().getStatus()) {
                    log.warn(String.format("[JOB_EXECUTOR] execute job[%d] status wrong in cache", param.getJobTaskId()));
                    continue;
                }
                boolean result = jobManager.execute(param.getJobTaskId());
                // handle result
                // find next run time
                if (context.getScheduler().getSchedulerType() == SchedulerType.SCHEDULED) {
                    // create another JobExecuteContext
                    try {
                        jobManager.reScheduleJob(param.getJobTaskId());
                        jobManagerContext.removeJob(param.getJobTaskId());
                    } catch (Exception e) {
                        log.error("[JOB_EXECUTOR] reschedule | remove finished job from cache failed", e);
                    }
                }
            } else {
                try {
                    Thread.sleep(param.getNextExecuteTime() - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    log.error("[JOB_EXECUTOR] timer failed", e);
                    break;
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("[JOB_EXECUTOR] thread stoped");
        }
    }

    public void turnOffAuto() {
        this.trigger = false;
    }
}
