package sun.spring.scheduler.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.spring.scheduler.core.*;
import sun.spring.scheduler.dao.DataAccessOperation;
import sun.spring.scheduler.dao.OperationCallback;
import sun.spring.scheduler.domain.JobEntity;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by root on 2016/3/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class DispatcherTest {

    @Autowired
    private DataAccessOperation<JobEntity, String> dataAccessOperation;
    final String jobName = "demoScheduleJobDispatcher";

    @Test
    public void testLock(){
        boolean result = dataAccessOperation.pessimisticLockQuery(jobName, new OperationCallback<JobEntity, Boolean>() {
            @Override
            public Boolean process(JobEntity jobEntity) {
                if (jobEntity.getRunFlag() != JobRunFlag.HANGUP.getValue() &&
                        jobEntity.getJobLock() == EntranceGuard.RELEASE.getStatus()) {
                    if (dataAccessOperation.updateLockStatus(jobName, EntranceGuard.LOCK))
                        return true;
                } else {
                    return false;
                }
                return false;
            }
        });
        assertTrue(result);
    }

    @Test
    public void testUpdateStatus(){
        boolean result = dataAccessOperation.updateJobStatus(jobName, ScheduleStatus.COMPLETED);
        assertTrue(result);
    }

//
//    @Resource(name = "transferDataToRedisJob")
//    private Job job;
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private ScheduleJobDispatcher scheduleJobDispatcher;
//
//    @Test
//    public void testDispatcher() throws Exception {
//        scheduleJobDispatcher.run();
//    }
//
//    @Test
//    public void testBatchJob() throws Exception {
//        new JobCleaner(jdbcTemplate,DBType.MYSQL).cleanBeforeJobLaunch(job.getName());
//        Map<String, JobParameter> parameterMap = new HashMap<>();
//        parameterMap.put("_age", new JobParameter(30L, false));
//        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
//        String exitCode = jobExecution.getExitStatus().getExitCode();
//        while (jobExecution.isRunning()) {
//            System.out.println("================" + exitCode);
//            System.out.println("isRunning======" + jobExecution.isRunning());
//            System.out.println("isTopping===="+jobExecution.isStopping());
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Test
//    public void testClean(){
//        new JobCleaner(jdbcTemplate, DBType.MYSQL).cleanBeforeJobLaunch(job.getName());
//    }
}