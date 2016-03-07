package sun.spring.scheduler.batch;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * Created by root on 2016/3/7.
 */
public interface Trigger extends BeanNameAware {

    boolean trigger();

    void onFire();

    ThreadPoolTaskExecutor getTreadPoolTaskExecutor();

    void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor executor);

    void fire(Map<Job, Guard> jobs);

    Map<Job, Guard> getJobs();

    void setJobs(Map<Job, Guard> jobGuardMap);

    String getName();

    void summary(Map<Job, Guard> jobGuardMap);


}
