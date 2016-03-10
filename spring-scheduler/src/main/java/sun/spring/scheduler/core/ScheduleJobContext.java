package sun.spring.scheduler.core;

import java.util.Date;
import java.util.Map;

/**
 * Created by root on 2016/3/9.
 */
public class ScheduleJobContext {
    private Date startTime;
    private Date endTime;
    private JobStatus status = JobStatus.WAIT;
    private String jobName;
    private String jobGroup;
    private String jobClassName;
    private int lock = EntranceGuard.RELEASE.getStatus();;
    private String scheduleName;

    private Map<String,Object> jobRetVal;

    private ScheduleJobConfig scheduleJobConfig;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public ScheduleJobConfig getScheduleJobConfig() {
        return scheduleJobConfig;
    }

    public void setScheduleJobConfig(ScheduleJobConfig scheduleJobConfig) {
        this.scheduleJobConfig = scheduleJobConfig;
    }

    public Map<String, Object> getJobRetVal() {
        return jobRetVal;
    }

    public void setJobRetVal(Map<String, Object> jobRetVal) {
        this.jobRetVal = jobRetVal;
    }
}
