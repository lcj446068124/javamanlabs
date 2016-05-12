package sun.spring.scheduler.core;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by sunyamorn on 3/6/16.
 */
public interface ScheduleTask extends Callable<Map<String, Object>>, ScheduleJobContextAware,
        BeanNameAware, InitializingBean {

    boolean preCondition();

    Map<String, Object> doTask(JobContext jobContext);

}
