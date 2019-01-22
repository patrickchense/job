package com.chenzhe.job.dao;

import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTaskRepository extends JpaRepository<JobTask, Long>{

    @Modifying(clearAutomatically = true)
    @Query("update JobTask set status = :newStatus where id = :jobId and status = :oldStatus")
    void updateStatus(@Param("jobId")Long jobId, @Param("oldStatus")JobStatus oldStatus, @Param("newStatus")JobStatus newStatus);
}