package com.chenzhe.job.service;

import org.springframework.beans.factory.InitializingBean;

/*
JobInitializer in charge of scan the file for job annotations
 */
public class JobInitializer implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO reload unfinished JOB TASK from db
        // TODO start thread to watch queue
    }
}
