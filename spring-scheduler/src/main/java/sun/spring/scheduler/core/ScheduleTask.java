package sun.spring.scheduler.core;

/**
 * Created by sunyamorn on 3/6/16.
 */
public interface ScheduleTask extends Runnable, TaskStatusAware {

    void before();

    void doTask(TaskContext taskContext);

    void after();


}
