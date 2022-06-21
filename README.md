# Job-Scheduler Design Demo
This is a job scheduler implemented by springboot,quartz and json MockDB.
# Involved Tech and Application Features
* springboot for rest api,quartz for job scheduler, json operation for Mock DB
* Eligible to Create/Read/Update/Delete jobs to run and restfuly api provided.
* Supports one time execution and repetitive executions triggered at a fixed interval,
  i.e. 10 seconds.
* Use JSON File as DB-Mock to persist jobs.
* The application ultilize quartz thread pool to scale to thousands of jobs and many workers.
* dockerized running available 

# API Guide:
- **Create a job**: 
`http://hostip:8080/api/addjob`
  ###### Rest Method
  > POST   <br />   
###### request body example as below(json): 
``` 
{
    "jobName": "job1",
    "jobDesc": "job1 task created!",
    "interval": 5
}
```
  **interval**: 0  &nbsp;==> one time job  
  **interval**: positive integer ==>  &nbsp;  repetitive job,
 
- **Query a job by job name:**
`http://hostip:8080/api/getjob/{jobName}`
  ###### Rest Method
  > GET   <br />  
 
- **Update a job(reschedule job by new interval and change job description):**  
   **Api Url**: `http://hostip:8080/api/updatejob`
  ###### Rest Method
  > POST   <br />   
###### request body example as below(json): 
``` 
{
    "jobName": "job1",
    "jobDesc": "job1 task updated",
    "interval": 10
}
```
  **interval**: 0  &nbsp; ==> one time job  
  **interval**: positive integer ==>  &nbsp;  repetitive job  
  
- **Delete a job**
`http://hostip:8080/api/deljob/{jobName}`
  ###### Rest Method
  > GET   <br />  
- **Pause a job**
`http://hostip:8080/api/pausejob/{jobName}`
  ###### Rest Method
  > GET   <br />  

- **Resume a job**
`http://hostip:8080/api/resumejob/{jobName}`
  ###### Rest Method
  > GET   <br />  
- **Pause all jobs**
`http://hostip:8080/api/pauseall`
  ###### Rest Method
  > GET   <br />  
- **Resume all jobs**
`http://hostip:8080/api/resumeall`
  ###### Rest Method
  > GET   <br />  
- **General Response Example**
```
{
"code": 200,
"message": "SUCCESS",
"data": {
"job2": "{\"interval\":5,\"jobDesc\":\"job2 updated !\",\"jobName\":\"job2\",\"status\":\"running\"}",
"job1": "{\"interval\":2,\"jobDesc\":\"job1 start!\",\"jobName\":\"job1\",\"status\":\"running\"}"
    }
}
```

# Dockerized Run Mode Guide:
- **cd to project root dir and run below commands:**
```
docker build -t yuanbest/job-scheduler-test .
docker run -dt -p 8080:8080 yuanbest/job-scheduler-test
```
Then you have build docker image and ran the container.

- **Actual running scene as below**

![jobscheduler-docker-run-1.PNG](https://s2.loli.net/2022/06/21/ijbA91LGd52gHVs.png)

![jobscheduler-docker-run-2.PNG](https://s2.loli.net/2022/06/21/9rp74C2eWSZUynv.png)

![jobscheduler-docker-run-3.PNG](https://s2.loli.net/2022/06/21/a4gSIFlEfcUtjrZ.png)