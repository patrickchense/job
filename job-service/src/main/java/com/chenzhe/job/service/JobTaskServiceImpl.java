package com.chenzhe.job.service;

import com.chenzhe.job.dao.JobSchedulerRepository;
import com.chenzhe.job.dao.JobTaskRepository;
import com.chenzhe.job.entity.JobStatus;
import com.chenzhe.job.entity.JobTask;
import com.chenzhe.job.query.QueryJobTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobTaskServiceImpl implements JobTaskService{

    @Autowired
    private JobTaskRepository jobTaskRepository;

    @Autowired
    private JobSchedulerRepository jobSchedulerRepository;

    @Override
    public List<JobTask> findJobTasks(final QueryJobTask queryJobTask) {
        return jobTaskRepository.findAll(buildSpecification(queryJobTask));
    }

    private Specification<JobTask> buildSpecification(final QueryJobTask queryJobTask) {
        return (Specification<JobTask>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(queryJobTask.getName())) {
                predicates.add(criteriaBuilder.equal(root.get("name"), queryJobTask.getName()));
            }
            if (queryJobTask.getId() != null && queryJobTask.getId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("id"), queryJobTask.getId()));
            }
            if (!StringUtils.isEmpty(queryJobTask.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), queryJobTask.getType()));
            }
            if (queryJobTask.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), queryJobTask.getStatus()));
            }
            if (queryJobTask.getCreateTimeStart() > 0 ) {
                predicates.add(criteriaBuilder.ge(root.get("createTime"), queryJobTask.getCreateTimeStart()));
            }
            if (queryJobTask.getCreateTimeEnd() > 0 ) {
                predicates.add(criteriaBuilder.le(root.get("createTime"), queryJobTask.getCreateTimeEnd()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    @Override
    public List<JobTask> findJobTasksByPage(final QueryJobTask queryJobTask) {
        Pageable pageable = PageRequest.of(queryJobTask.getPage(), queryJobTask.getPageSize());
        Page page = jobTaskRepository.findAll(buildSpecification(queryJobTask), pageable);

        //page.getTotalElements();
        //page.getTotalPages();

        return page.getContent();
    }

    @Override
    public JobTask save(JobTask task) {
       return jobTaskRepository.save(task);
    }

    @Override
    public void updateTaskStatus(Long jobId, JobStatus oldStatus, JobStatus newStatus) {
        jobTaskRepository.updateStatus(jobId, oldStatus, newStatus);
    }

    @Override
    public JobTask findById(Long jobId) {
        return jobTaskRepository.findById(jobId).get();
    }

}
