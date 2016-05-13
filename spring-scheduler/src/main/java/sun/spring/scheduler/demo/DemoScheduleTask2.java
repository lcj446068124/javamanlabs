package sun.spring.scheduler.demo;

import sun.spring.scheduler.core.AbstractScheduleTask;
import sun.spring.scheduler.core.JobContext;

import java.util.Collections;
import java.util.Map;

/**
 * Created by yamorn on 2016/5/13.
 */
public class DemoScheduleTask2 extends AbstractScheduleTask {
    @Override
    public boolean preCondition() {
        return true;
    }

    @Override
    public Map<String, Object> doTask(JobContext jobContext) {
        System.out.println("hahah");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
}
