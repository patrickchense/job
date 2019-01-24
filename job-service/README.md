## job-service
### Goal
create a job management service.Provides functions like job store, job execute and job status find.

### Design
#### JobRepository

#### JobService

### How to use?
1. provide JobManager Service for third party, using register & start
2. execution thread will monitor the job task queue
3. reload all the unfinished tasks if system crashed.

#### through annotation
TODO  
could using annotation to make any method run in a scheduled way.

#### through interface
could impl the Job interface instead of create the job task anonymously.

### Configuration
1. execution thread pausing period
2. queue size
3. execution thread trigger

#### Database
Using memory database H2 here. Could change to any other relation database, like Mysql or PG.


