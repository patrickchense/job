package com.chenzhe.job.context;

import com.chenzhe.job.entity.JobScheduler;
import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.service.Job;
import lombok.Data;

@Data
public class JobExecuteContext {

    // basic info
    private JobTask task;

    // exectue func
    private Job job;

    // running schedule
    private JobScheduler scheduler;

    public void execute() throws Exception {
        job.execute();
    }
}
