package com.chenzhe.job.service;

import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.context.JobQueueParam;
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
    /*
    pause time default 1000ms
     */
    @Value("${job.execute.pause:5000")
    private long pauseTime;

    @Override
    public void run() {
        while(true) {
            JobQueueParam param = JobManager.JOB_EXECUTE_QUEUE.peek();
            if (param.getNextExecuteTime() <= System.currentTimeMillis()) {
                JobManager.JOB_EXECUTE_QUEUE.poll();
                // status change
                JobExecuteContext context = JobManager.JOBS.get(param.getJobTaskId());
                try {
                    context.getJob().execute();
                    // status change
                } catch (Exception e) {
                    //TODO job log
                    log.error(String.format("[JOB_EXECUTOR] execute job[%d] failed", param.getJobTaskId()), e);
                    // status change
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
