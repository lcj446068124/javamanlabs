package sun.batch.tutorial.service;

import org.springframework.batch.core.JobExecution;

/**
 * Created by root on 2016/3/3.
 */
public interface BatchDemoService {
    JobExecution transfer();
}
