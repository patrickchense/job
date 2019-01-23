package com.chenzhe.job.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
JobInitializer in charge of scan the file for job annotations
 */
@Slf4j
@Component
public class JobInitializer implements InitializingBean {

    @Autowired
    private JobExecuteThread jobExecuteThread;

    @Autowired
    private JobManager jobManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (log.isInfoEnabled()) {
            log.info("start job execute thread");
        }
        jobExecuteThread.start();
        jobManager.reloadJobTasks();
    }
}
