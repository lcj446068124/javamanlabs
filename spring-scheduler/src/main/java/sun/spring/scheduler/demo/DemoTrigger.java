package sun.spring.scheduler.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sun.spring.scheduler.batch.AbstractTrigger;
import sun.spring.scheduler.batch.Guard;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by root on 2016/3/7.
 */
public class DemoTrigger extends AbstractTrigger {

    private JobLauncher jobLauncher;

    private ThreadPoolTaskExecutor executor;

    private Map<Job, Guard> jobGuardMap;

    @Override
    public boolean trigger() {
        return true;
    }


    @Override
    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    @Override
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Override
    public Map<Job, Guard> getJobs() {
        return jobGuardMap;
    }

    @Override
    public void setJobs(Map<Job, Guard> jobGuardMap) {
        this.jobGuardMap = jobGuardMap;
    }

    @Override
    public ThreadPoolTaskExecutor getTreadPoolTaskExecutor() {
        return executor;
    }

    @Override
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }
}
