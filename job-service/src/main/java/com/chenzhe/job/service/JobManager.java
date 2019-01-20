package com.chenzhe.job.service;

import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.context.JobQueueParam;
import com.chenzhe.job.entity.JobPriority;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JobManager {

    @Value("${job.manager.queue.size:20}")
    private static int QUEUE_SIZE;

    protected static PriorityQueue<JobQueueParam> JOB_EXECUTE_QUEUE = new PriorityQueue<>(QUEUE_SIZE, (param1, param2) -> (int) (param1.getNextExecuteTime().equals(param2.getNextExecuteTime()) ? JobPriority.compare(param1.getPriority(), param2.getPriority()) :param1.getNextExecuteTime() - param2.getNextExecuteTime()));

    protected static ConcurrentHashMap<Long, JobExecuteContext> JOBS = new ConcurrentHashMap<>();
    /*
    register job
    schedule a next execution time or call it immediately

     */
    public JobExecuteContext registerJob(String name, String type, JobPriority jobPriority, Job job) throws Exception {
        JobExecuteContext context = new JobExecuteContext();

        return context;
    }

    /*
    check schedule type
    put job into queue
     */
    public void startJob(JobExecuteContext context) throws Exception {

    }

    /*
        rightnow & once job should run directly
        scheduled job should put into queue wait for manager to run the job
     */
    public void execute(JobExecuteContext context) throws Exception {

    }


}
