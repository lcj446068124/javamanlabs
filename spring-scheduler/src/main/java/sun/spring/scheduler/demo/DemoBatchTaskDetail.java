package sun.spring.scheduler.demo;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import sun.spring.scheduler.batch.AbstractBatchScheduleTask;
import sun.spring.scheduler.core.ScheduleTaskContext;

/**
 * Created by root on 2016/3/9.
 */
public class DemoBatchTaskDetail extends AbstractBatchScheduleTask {
    @Override
    public void doBatchTask(ScheduleTaskContext scheduleTaskContext) {
        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(job, new JobParameters());
            while (!"COMPLETED".equals(jobExecution.getExitStatus().getExitCode())) {
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
    }

    @Override
    public boolean precondition() throws Exception {
        return true;
    }
}
