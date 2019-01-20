package com.chenzhe.job.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "job_task")
@Data
public class JobTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public int type;

    public JobPriority priority;

    public JobStatus status;

    public String name;

    public long createTime;

    public long updateTime;
}
