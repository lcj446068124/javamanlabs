package sun.spring.scheduler.core;

import java.util.List;

/**
 * Created by root on 2016/3/9.
 */
public interface ScheduleJobDispatcher extends Runnable {

    void beforeJob();

    void setScheduleTasks(List<ScheduleTask> scheduleTasks);

    void afterJob();

    void setScheduleJobListener(ScheduleJobListener scheduleJobListener);
}
