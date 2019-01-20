package com.chenzhe.job.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "job_task")
@Data
public class JobTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int type;

    private JobPriority priority;

    private JobStatus status;

    private String name;

    private long createTime;

    private long updateTime;
}
