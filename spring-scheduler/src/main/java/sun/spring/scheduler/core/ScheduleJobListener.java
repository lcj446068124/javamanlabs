package sun.spring.scheduler.core;

/**
 * Created by root on 2016/3/10.
 */
public interface ScheduleJobListener {

    void beforeJob(ScheduleJobContext scheduleJobContext);

    void afterJob(ScheduleJobContext scheduleJobContext);
}
