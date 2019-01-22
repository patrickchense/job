package com.chenzhe.job.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "job_scheduler")
public class JobScheduler {

    @Id
    @GeneratedValue
    private Long id;

    private Long jobTaskId;

    private SchedulerType schedulerType;

    private Long nextExecuteTime;

    private Long period;

    private long createTime;
}
