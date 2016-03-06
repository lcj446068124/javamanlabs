package sun.spring.scheduler.core;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by sunyamorn on 3/6/16.
 */
public class SimpleScheduleTask extends AbstractScheduleTask {

    protected ScheduleTaskConfig scheduleTaskConfig;

    @Override
    public void doTask(TaskContext taskContext) {
        // overwrite this method
    }

    @Override
    public ScheduleTaskConfig getScheduleTaskConfig() {
        return this.scheduleTaskConfig;
    }

    public void setScheduleTaskConfig(ScheduleTaskConfig scheduleTaskConfig) {
        this.scheduleTaskConfig = scheduleTaskConfig;
    }
}
