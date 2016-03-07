package sun.batch.tutorial.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by root on 2016/3/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class ParallelStepTransferTest {

    @Autowired
    private ParallelStepTransfer parallelStepTransfer;

    @Autowired
    private TransferPersonJob transferPersonJob;

    @Test
    public void testTransfer() throws Exception {
        JobExecution jobExecution = parallelStepTransfer.transfer();
        while (!"COMPLETED".equals(jobExecution.getExitStatus().getExitCode())) {
            System.out.println(jobExecution.getExitStatus().getExitCode());
            Thread.sleep(1000);
        }
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }

    @Test
    public void testMultipleJob() throws Exception{
        JobExecution jobExecutionA = parallelStepTransfer.transfer();

        JobExecution jobExecutionB = transferPersonJob.transfer();

        while (!"COMPLETED".equals(jobExecutionA.getExitStatus().getExitCode()) &&
                !"COMPLETED".equals(jobExecutionB.getExitStatus().getExitCode())) {
            System.out.println(jobExecutionA.getExitStatus().getExitCode());
            Thread.sleep(1000);
        }
    }
}