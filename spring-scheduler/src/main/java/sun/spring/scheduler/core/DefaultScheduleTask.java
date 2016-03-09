package sun.spring.scheduler.core;

/**
 * Created by root on 2016/3/9.
 *
 * This bean's scope must be prototype
 */
public class DefaultScheduleTask extends AbstractScheduleTask {

    @Override
    public void setScheduleTaskDetail(ScheduleTaskDetail scheduleTaskDetail) {
        this.scheduleTaskDetail = scheduleTaskDetail;
    }
}
