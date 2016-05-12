package sun.spring.scheduler.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yamorn on 2016/5/12.
 */
public final class JobContext {
    private String jobName;
    private ScheduleStatus scheduleStatus = ScheduleStatus.WAIT;
    private Date startTime;
    private Date endTime;
    private String jobGroup;
    private String jobClassName;
    private int jobLock = EntranceGuard.RELEASE.getStatus();
    ;
    private String scheduleName;
    private Map<String, Object> jobRetVal = new HashMap<>();
    private Exception exception = null;

    public String getJobName() {
        return jobName;
    }

    public JobContext setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public JobContext setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public JobContext setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public JobContext setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public JobContext setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        return this;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public JobContext setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
        return this;
    }

    public int getJobLock() {
        return jobLock;
    }

    public JobContext setJobLock(int jobLock) {
        this.jobLock = jobLock;
        return this;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public JobContext setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        return this;
    }

    public Map<String, Object> getJobRetVal() {
        return jobRetVal;
    }

    public JobContext setJobRetVal(Map<String, Object> jobRetVal) {
        this.jobRetVal = jobRetVal;
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public JobContext setException(Exception exception) {
        this.exception = exception;
        return this;
    }
}
