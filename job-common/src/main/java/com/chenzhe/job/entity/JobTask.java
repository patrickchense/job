package com.chenzhe.job.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "job_task")
@Data
@NoArgsConstructor
public class JobTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private JobPriority priority;

    private JobStatus status;

    private String name;

    private long createTime;

    private long updateTime;

    public JobTask(String type, JobPriority priority, JobStatus status, String name, long createTime) {
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.name = name;
        this.createTime = createTime;
    }
}
