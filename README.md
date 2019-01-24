##Job Management
##Module Design
![design thinkings](docs/desgin.jpeg)


##job-common
all the basic entities and query beans

##job-service
most important module
1. all the daos
2. all the services
3. JobManager, maintain the job task queue and map.
4. JobExecuteThread, watchs the job task queue and run the job in time.


##job-web
provide all kinds of apis
1. query job tasks
2. query logs


##demo
show how to use job management system in the third party projects



##Meet Requirements
1. Flexibility
supports all the kinds of jobs running as long as though impl the Job interface.
2. Reliability
no side-effects. All the jobs after run either success or failed. (Job status changed)
3. Internal Consistency
all the job task status traced.
4. Priority
using priority queue to do that.
5. Scheduling
provide three types of scheduling. 
 * running immediately
 * running once at a specific time.
 * running based on a scheduled period. 
 
 ##Improvement
 1. more apis in the jobManager
 2. more apis in web
 3. support annotation registration job tasks and schedule
 4. ...
