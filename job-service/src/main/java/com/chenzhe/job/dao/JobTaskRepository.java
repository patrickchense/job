package com.chenzhe.job.dao;

import com.chenzhe.job.entity.JobTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTaskRepository extends JpaRepository<JobTask, Long>{

}