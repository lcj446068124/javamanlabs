package sun.spring.scheduler.core;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import javax.batch.runtime.context.JobContext;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by sunyamorn on 3/6/16.
 */
public interface ScheduleTask extends Callable<Map<String, Object>>, TaskStatusAware, ScheduleJobContextAware,
        BeanNameAware, InitializingBean {

    void beforeTask();

    void afterTask();

    void setScheduleTaskDetail(ScheduleTaskDetail scheduleTaskDetail);

}
