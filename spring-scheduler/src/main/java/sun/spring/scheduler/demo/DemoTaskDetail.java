package sun.spring.scheduler.demo;

import sun.spring.scheduler.core.ScheduleTaskContext;
import sun.spring.scheduler.core.ScheduleTaskDetail;

import java.util.Date;
import java.util.Map;

/**
 * Created by root on 2016/3/9.
 */
public class DemoTaskDetail implements ScheduleTaskDetail {
    @Override
    public boolean preCondition() throws Exception {
        return true;
    }

    @Override
    public Map<String,Object> doTask(ScheduleTaskContext scheduleTaskContext) {
        System.out.println("task=====" + new Date().toLocaleString() +" " + Thread.currentThread().getName());
        return null;
    }
}
