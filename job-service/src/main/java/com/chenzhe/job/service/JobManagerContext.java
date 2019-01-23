package com.chenzhe.job.service;

import com.chenzhe.job.context.JobExecuteContext;
import com.chenzhe.job.context.JobQueueParam;
import com.chenzhe.job.entity.JobPriority;
import com.chenzhe.job.entity.JobStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Component
@Data
public class JobManagerContext {

    @Value("${job.manager.queue.size}")
    private Integer QUEUE_SIZE = 20;

    private PriorityBlockingQueue<JobQueueParam> JOB_EXECUTE_QUEUE = new PriorityBlockingQueue<>(QUEUE_SIZE, (param1, param2) -> (int) (param1.getNextExecuteTime().equals(param2.getNextExecuteTime()) ? JobPriority.compare(param1.getPriority(), param2.getPriority()) :param1.getNextExecuteTime() - param2.getNextExecuteTime()));

    private ConcurrentHashMap<Long, JobExecuteContext> JOBS = new ConcurrentHashMap<>();

    void clear() {
        JOB_EXECUTE_QUEUE.clear();
        JOBS.clear();
    }

    JobExecuteContext getJobExecuteContext(Long jobId) {
        return JOBS.get(jobId);
    }

    boolean containsJobExecuteContext(Long jobId) {
        return JOBS.containsKey(jobId);
    }

    void addJobExecuteContext(Long jobId, JobExecuteContext context) {
        JOBS.put(jobId, context);
    }

    void addJobToQueue(JobQueueParam param) {
        JOB_EXECUTE_QUEUE.add(param);
    }

    public boolean removeJob(Long jobId) throws Exception {
        if (JOBS.contains(jobId)) {
            JobExecuteContext context = JOBS.get(jobId);
            if (JobStatus.RUNNING == context.getTask().getStatus()) {
                throw new Exception("can't remove running tasks");
            }
            JobQueueParam param = new JobQueueParam();
            param.setJobTaskId(jobId);
            param.setNextExecuteTime(context.getScheduler().getNextExecuteTime());
            param.setPriority(context.getTask().getPriority());
            JOB_EXECUTE_QUEUE.remove(param);
            JOBS.remove(jobId);
            return true;
        }
        return false;
    }

    JobQueueParam peekJobParam() {
        return JOB_EXECUTE_QUEUE.peek();
    }

    JobQueueParam pollJobParam() {
        return JOB_EXECUTE_QUEUE.poll();
    }
}
