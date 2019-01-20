package com.chenzhe.job.entity;

import lombok.Data;

import javax.persistence.*;

/*
use to save log to db
 */
@Entity
@Table
@Data
public class JobLog {

    @Id
    @GeneratedValue
    public Long id;

    public long createTime;

    @Column(length = 1000)
    public String description;
}
