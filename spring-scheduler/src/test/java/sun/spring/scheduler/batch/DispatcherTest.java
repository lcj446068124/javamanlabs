package sun.spring.scheduler.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.spring.scheduler.core.ScheduleJobDispatcher;

import javax.annotation.Resource;

/**
 * Created by root on 2016/3/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class DispatcherTest {

    @Resource(name = "transferDataToRedisJob")
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ScheduleJobDispatcher scheduleJobDispatcher;

    @Test
    public void testDispatcher() throws Exception {
        scheduleJobDispatcher.run();
    }

    @Test
    public void testBatchJob() throws Exception {
        new JobCleaner(jdbcTemplate).cleanBeforeJobLaunch(job.getName());
        JobExecution jobExecution = null;
        jobExecution = jobLauncher.run(job, new JobParameters());
        String exitCode = jobExecution.getExitStatus().getExitCode();
        while (jobExecution.isRunning()) {
            System.out.println("================" + exitCode);
            System.out.println("isRunning======" + jobExecution.isRunning());
            System.out.println("isTopping===="+jobExecution.isStopping());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}