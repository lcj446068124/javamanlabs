package sun.spring.scheduler.core;

import java.util.List;

/**
 * Created by root on 2016/3/9.
 */
public interface ScheduleJobDispatcher extends Runnable {

    void beforeJob();

    void afterJob(boolean hasException);

    void setScheduleTasks(List<ScheduleTask> scheduleTasks);

    void setJobListener(JobListener jobListener);
}
