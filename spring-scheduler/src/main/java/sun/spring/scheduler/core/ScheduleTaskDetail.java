package sun.spring.scheduler.core;

import java.util.Map;

/**
 * Created by root on 2016/3/9.
 */
public interface ScheduleTaskDetail {

    boolean preCondition() throws Exception;

    Map<String, Object> doTask(ScheduleTaskContext scheduleTaskContext);

}
