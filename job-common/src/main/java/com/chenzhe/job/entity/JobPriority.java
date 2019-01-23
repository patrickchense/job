package com.chenzhe.job.entity;

/*
@Description use to mark job priority
 */
public enum JobPriority {
    LOW(1),MID(2),HIGH(3),URGENT(4);

    private int level;

    JobPriority(int level) {
        this.level = level;
    }

    public static int compare(JobPriority priority1, JobPriority priority2) {
        return priority1.level < priority2.level ? 1 : (priority1.level == priority2.level ? 0 : -1);
    }
}
