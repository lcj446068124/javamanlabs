package sun.spring.scheduler.demo;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import sun.spring.scheduler.batch.AbstractBatchScheduleTask;
import sun.spring.scheduler.core.DBType;
import sun.spring.scheduler.core.ScheduleTaskContext;

import java.util.Map;

/**
 * Created by root on 2016/3/9.
 */
public class DemoBatchTaskDetail extends AbstractBatchScheduleTask {

    @Override
    public boolean preCondition() throws Exception {
        return true;
    }

    @Override
    public void setJobParameters(JobParameters jobParameters) {

    }

    @Override
    public JobParameters getJobParameters() {
        return null;
    }

    @Override
    public DBType getDBType() {
        return DBType.MYSQL;
    }
}
