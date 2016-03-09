package sun.spring.scheduler.core;

import java.util.List;

/**
 * Created by root on 2016/3/9.
 *
 * This bean's scope must be prototype
 */
public class DefaultScheduleJobDispatcher extends AbstractScheduleJobDispatcher {
    @Override
    public void setScheduleTasks(List<ScheduleTask> scheduleTasks) {
        this.scheduleTasks = scheduleTasks;
    }
}
