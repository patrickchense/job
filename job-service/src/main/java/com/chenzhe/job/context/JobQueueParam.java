package com.chenzhe.job.context;

import com.chenzhe.job.entity.JobPriority;
import lombok.Data;

@Data
public class JobQueueParam {

    private Long jobTaskId;

    private Long nextExecuteTime;

    private JobPriority priority;
}
