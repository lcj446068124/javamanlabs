package sun.spring.scheduler.core;

/**
 * Created by root on 2016/3/9.
 */
public interface ScheduleTaskDetail {

    boolean precondition() throws Exception;

    void doTask(ScheduleTaskContext scheduleTaskContext);

}
