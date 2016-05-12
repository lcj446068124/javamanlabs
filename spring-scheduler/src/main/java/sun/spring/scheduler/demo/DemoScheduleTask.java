package sun.spring.scheduler.demo;

import sun.spring.scheduler.core.AbstractScheduleTask;
import sun.spring.scheduler.core.JobContext;
import sun.spring.scheduler.core.ScheduleTask;

import java.util.Map;

/**
 * Created by yamorn on 2016/5/12.
 */
public class DemoScheduleTask extends AbstractScheduleTask{
    @Override
    public boolean preCondition() {
        return true;
    }

    @Override
    public Map<String, Object> doTask(JobContext jobContext) {
        System.out.println("hello");
        return null;
    }
}
