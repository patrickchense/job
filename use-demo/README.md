##Goal
use-demo only have the simple demo code to use job systems.

##How
There is two ways to use job systems by desgin
1. depend job-service in the project and use the the jobManager service to register & start & execute jobs.
2. call job-web api to do the same (by design, code not ready)

##Tests
1. testSingleJob, unit test. shows how to register and start a job.
2. testPriorityJob, unit test.shows how the job system supports priorities.

