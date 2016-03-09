package sun.spring.scheduler.batch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Created by sunyamorn on 3/8/16.
 */
public class SimpleJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            //job success
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            //job failure
        }
    }
}
