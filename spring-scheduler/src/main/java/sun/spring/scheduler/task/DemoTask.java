package sun.spring.scheduler.task;

import org.springframework.stereotype.Component;
import sun.spring.scheduler.core.SimpleScheduleTask;
import sun.spring.scheduler.core.TaskContext;

import java.util.Date;

/**
 * Created by sunyamorn on 3/6/16.
 */
public class DemoTask extends SimpleScheduleTask{
    @Override
    public void doTask(TaskContext taskContext) {
        super.doTask(taskContext);
        System.out.println("do task " + new Date().toLocaleString());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
