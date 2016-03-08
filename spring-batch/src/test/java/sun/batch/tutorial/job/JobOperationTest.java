package sun.batch.tutorial.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 2016/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class JobOperationTest {
    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private JobExplorer jobExplorer;

    @Test
    public void testJobOperator() throws Exception {
        Set<String> jobNames = jobOperator.getJobNames();
        for (String name : jobNames) {
            Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(name);
            for (JobExecution jobExecution : jobExecutions) {
                Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
                for (StepExecution stepExecution : stepExecutions) {
                    String stepSummary = stepExecution.getSummary();
                    System.out.println("[[[==" + stepSummary);

                }
                String summary = jobOperator.getSummary(jobExecution.getId());

                System.out.println(summary);
            }
        }
    }
}
