package sun.spring.scheduler.demo;

import sun.spring.scheduler.core.AbstractScheduleTask;
import sun.spring.scheduler.core.TaskContext;

import java.util.Date;

/**
 * Created by sunyamorn on 3/6/16.
 */
public class DemoTask extends AbstractScheduleTask {
    @Override
    public void doTask(TaskContext taskContext) {
        System.out.println("do task " + new Date().toLocaleString());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
