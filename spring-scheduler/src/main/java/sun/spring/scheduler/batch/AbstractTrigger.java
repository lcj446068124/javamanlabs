package sun.spring.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;

/**
 * Created by root on 2016/3/7.
 */
public abstract class AbstractTrigger implements Trigger {

    protected String name;

    private final Object lock = new Object();

    private static final Logger logger = LoggerFactory.getLogger(AbstractTrigger.class);

    @Override
    public void onFire() {
        if (!trigger()) {
            return;
        }

        synchronized (lock){
            Map<Job, Guard> jobs = getJobs();
            if(jobs == null || jobs.size() == 0)
                return;

            summary(jobs);

            fire(jobs);
        }

    }

    @Override
    public void summary(Map<Job, Guard> jobGuardMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("Batch Trigger Summary:\n");
        sb.append("\tTrigger Events:\n");
        for (Map.Entry<Job, Guard> entry : jobGuardMap.entrySet()) {
            sb.append("\t\tJob Name: ").append(entry.getKey().getName());
            sb.append("\tGuard Name: ").append(entry.getValue().getName());
            sb.append("\n");
        }
        logger.debug(sb.toString());
    }

    @Override
    public void fire(Map<Job, Guard> jobs) {
        if (jobs == null || jobs.size() == 0)
            return;

        for (Map.Entry<Job, Guard> entry : jobs.entrySet()) {
            final Job job = entry.getKey();
            final Guard guard = entry.getValue();

            getTreadPoolTaskExecutor().submit(new Callable<JobExecution>() {
                @Override
                public JobExecution call() throws Exception {
                    JobExecution jobExecution = null;
                    try {
                        guard.lock();
                        jobExecution = getJobLauncher().run(job, new JobParameters());
                        while (!"COMPLETED".equals(jobExecution.getExitStatus().getExitCode())) {
                            System.out.println("inner: " +jobExecution.getExitStatus().getExitCode());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JobExecutionAlreadyRunningException e) {
                        e.printStackTrace();
                    } catch (JobRestartException e) {
                        e.printStackTrace();
                    } catch (JobInstanceAlreadyCompleteException e) {
                        e.printStackTrace();
                    } catch (JobParametersInvalidException e) {
                        e.printStackTrace();
                    } finally {
                        guard.release();
                    }
                    return jobExecution;
                }
            });
        }
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    @Override
    public String getName(){
        return this.name;
    }

    public abstract JobLauncher getJobLauncher();

    public abstract void setJobLauncher(JobLauncher jobLauncher);

}
