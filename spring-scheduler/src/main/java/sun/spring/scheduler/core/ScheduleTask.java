package sun.spring.scheduler.core;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import javax.batch.runtime.context.JobContext;

/**
 * Created by sunyamorn on 3/6/16.
 */
public interface ScheduleTask extends Runnable, TaskStatusAware, ScheduleJobContextAware,
        BeanNameAware, InitializingBean {

    void beforeTask();

    void afterTask();

    void setScheduleTaskDetail(ScheduleTaskDetail scheduleTaskDetail);

}
