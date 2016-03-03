package sun.batch.tutorial.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by root on 2016/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})

public class BatchDemoServiceImplTest {

    @Autowired
    private BatchDemoService batchDemoService;

    @Test
    public void testTransfer() throws Exception {
        JobExecution jobExecution = batchDemoService.transfer();

        while (!"COMPLETED".equals(jobExecution.getExitStatus().getExitCode())){
            System.out.println(jobExecution.getExitStatus().getExitCode());
            Thread.sleep(1000);
        }
        System.out.println("done");
//        Thread.currentThread().join();
    }
}