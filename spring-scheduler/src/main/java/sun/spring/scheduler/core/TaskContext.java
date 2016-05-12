package sun.spring.scheduler.core;

import java.util.Date;

/**
 * Created by yamorn on 2016/5/12.
 */
public class TaskContext {
    private String taskName;
    private JobContext jobContext;
    private Exception exception;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public JobContext getJobContext() {
        return jobContext;
    }

    public void setJobContext(JobContext jobContext) {
        this.jobContext = jobContext;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
