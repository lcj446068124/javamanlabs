package sun.batch.tutorial.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * Created by root on 2016/3/8.
 */
public class AppJobExecutorListener extends JobExecutionListenerSupport {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("hhhhhhhhhhhhhhhhh==============");
    }
}
