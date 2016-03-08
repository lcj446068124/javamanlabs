package sun.batch.tutorial.job;

import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sun.batch.tutorial.entity.Person;
import sun.batch.tutorial.support.JobCleaner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 2016/3/4.
 */
@Component
public class TransferPersonJob {

    @Resource(name = "transferPersonToRedisJob")
    private Job job;

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JobExecution transfer() throws Exception {
        // clean job
        new JobCleaner(jdbcTemplate).cleanBeforeJobLaunch(job.getName());

        Map<String,JobParameter> params = new HashMap<>();
        params.put("jobName", new JobParameter(job.getName()));
        return jobLauncher.run(job, new JobParameters(params));
    }
}
