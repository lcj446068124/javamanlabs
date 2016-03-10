package sun.spring.scheduler.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.spring.scheduler.core.DBType;
import sun.spring.scheduler.core.ScheduleTaskContext;
import sun.spring.scheduler.core.ScheduleTaskDetail;

import java.util.Map;

/**
 * Created by root on 2016/3/9.
 */
public abstract class AbstractBatchScheduleTask implements ScheduleTaskDetail{

    protected JdbcTemplate jdbcTemplate;

    protected Job job;

    protected JobLauncher jobLauncher;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Override
    public Map<String,Object> doTask(ScheduleTaskContext scheduleTaskContext) {
        // clean job log
        new JobCleaner(jdbcTemplate, getDBType()).cleanBeforeJobLaunch(job.getName());

        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(job, getJobParameters());
            while (jobExecution.isRunning()) {
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
        }
        return null;
    }

    public abstract void setJobParameters(JobParameters jobParameters);

    public abstract JobParameters getJobParameters();

    public abstract DBType getDBType();

}
