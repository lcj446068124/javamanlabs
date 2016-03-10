package sun.spring.scheduler.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.spring.scheduler.core.DBType;
import sun.spring.scheduler.core.ScheduleTaskContext;
import sun.spring.scheduler.core.ScheduleTaskDetail;

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
    public void doTask(ScheduleTaskContext scheduleTaskContext) {
        // clean job log
        new JobCleaner(jdbcTemplate, DBType.MYSQL).cleanBeforeJobLaunch(job.getName());

        doBatchTask(scheduleTaskContext);
    }

    public abstract void doBatchTask(ScheduleTaskContext scheduleTaskContext);
}
