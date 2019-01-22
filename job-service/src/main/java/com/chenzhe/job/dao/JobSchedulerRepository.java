package com.chenzhe.job.dao;

import com.chenzhe.job.entity.JobScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSchedulerRepository extends JpaRepository<JobScheduler, Long> {

}
