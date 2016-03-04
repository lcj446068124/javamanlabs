package sun.batch.tutorial.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by root on 2016/3/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class TransferPersonJobTest {

    @Autowired
    private TransferPersonJob transferPersonJob;

    @Test
    public void testTransfer() throws Exception {
        JobExecution jobExecution = transferPersonJob.transfer();
        while (!"COMPLETED".equals(jobExecution.getExitStatus().getExitCode())) {
            System.out.println(jobExecution.getExitStatus().getExitCode());
            Thread.sleep(1000);
        }
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}