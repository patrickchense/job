package com.chenzhe.job.query;


import com.chenzhe.job.entity.JobPriority;
import com.chenzhe.job.entity.JobStatus;

public class QueryJobTask {

    public Long id;

    public int type;

    public JobPriority priority;

    public JobStatus status;

    public String name;

    public long createTimeStart;

    public long createTimeEnd;

    public int page;

    public int pageSize;

}
