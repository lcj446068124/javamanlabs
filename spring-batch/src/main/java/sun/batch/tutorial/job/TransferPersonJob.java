package sun.batch.tutorial.job;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.batch.tutorial.entity.Person;

import javax.annotation.Resource;

/**
 * Created by root on 2016/3/4.
 */
@Component
public class TransferPersonJob {

    @Resource(name = "transferPersonToRedisJob")
    private Job job;

    @Autowired
    private SimpleJobLauncher jobLauncher;

    public JobExecution transfer() throws Exception {

        return jobLauncher.run(job, new JobParameters());
    }
}