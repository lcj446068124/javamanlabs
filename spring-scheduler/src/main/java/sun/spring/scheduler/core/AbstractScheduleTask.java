package sun.spring.scheduler.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import sun.spring.scheduler.domain.TaskEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by sunyamorn on 3/6/16.
 *
 * Base class for all tasks.
 */
public abstract class AbstractScheduleTask implements ScheduleTask, BeanNameAware, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractScheduleTask.class);

    private Date startTime;
    private Date endTime;
    private TaskStatus status;
    private String taskName;
    private String taskGroup;
    private String taskClassName;
    private int lock = EntranceGuard.RELEASE.getStatus();
    private String scheduleName;

    private String tablePrefix = "ST_";

    private static final String TABLE_NAME = "CLUSTER_CONTROL";

    private final String[] fields = new String[]{"TASK_NAME", "TASK_STATUS", "TASK_LOCK", "TASK_GROUP", "TASK_CLASS_NAME", "SCHEDULE_NAME", "LAST_START_TIME", "LAST_END_TIME"};

    private TaskContext taskContext;

    protected ScheduleTaskConfig scheduleTaskConfig;



    @Override
    public void run() {
        if (!tryLock())
            return;

        before();

        try {
            doTask(taskContext);
        } catch (Exception e) {
            // ignore
        } finally {
            // release row lock
            release();
        }

        after();

    }

    @Override
    public void after() {
        this.endTime = new Date();
        this.lock = EntranceGuard.RELEASE.getStatus();
        this.status = TaskStatus.COMPLETED;
        refreshTaskContext();
    }

    @Override
    public void before() {
        this.startTime = new Date();
        this.lock = EntranceGuard.LOCK.getStatus();
        this.status = TaskStatus.RUNNING;
        refreshTaskContext();
    }

    @Override
    public TaskContext getTaskContext() {
        return this.taskContext;
    }

    private boolean tryLock() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(scheduleTaskConfig.getTransactionManager());
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                boolean result = true;
                try {
                    TaskEntity entity = lockRow();
                    if (entity != null && entity.getLock() == EntranceGuard.RELEASE.getStatus()) {
                        if (!updateGuardStatus(EntranceGuard.LOCK.getStatus(), fields[6])) {
                            logger.error("Update guard status failed.");
                        }
                    } else {
                        result = false;
                    }
                } catch (Exception e) {
                    result = false;
                    status.setRollbackOnly();
                }
                return result;
            }
        });
    }

    private boolean release() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(scheduleTaskConfig.getTransactionManager());
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                boolean result = true;
                try {
                    if (!updateGuardStatus(EntranceGuard.RELEASE.getStatus(), fields[7])) {
                        logger.error("Release row lock failed.");
                    }
                } catch (Exception e) {
                    result = false;
                    status.setRollbackOnly();
                }
                return result;
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert (scheduleTaskConfig != null);

        // change default value
        tablePrefix = scheduleTaskConfig.getTablePrefix();

        // init
        TaskEntity entity = queryForObject();
        if (entity == null) {
            // init task context
            status = TaskStatus.WAIT;
            ThreadGroup threadGroup = scheduleTaskConfig.getScheduler().getThreadGroup();
            if (threadGroup != null) {
                taskGroup = threadGroup.getName();
            }
            taskClassName = this.getClass().getName();
            lock = EntranceGuard.RELEASE.getStatus();
            scheduleName = scheduleTaskConfig.getScheduler().getThreadNamePrefix();
            if (!insert()) {
                logger.error("Initialize task record failed.");
            }
        } else {
            status = convert(entity.getStatus());
            taskClassName = entity.getTaskClassName();
            lock = entity.getLock();
            scheduleName = entity.getScheduleName();
            taskGroup = entity.getTaskGroup();
            startTime = entity.getStartTime();
            endTime = entity.getEndTime();
            // Validate run status and change all run status as wait status.
            validation();
        }

        taskContext = new TaskContext();
        refreshTaskContext();

        // register
        TaskRegistrationCenter.register(this);
    }

    @Override
    public void destroy() throws Exception {

    }

    private void refreshTaskContext() {
        taskContext.setScheduleName(this.scheduleName);
        taskContext.setEndTime(this.endTime);
        taskContext.setScheduler(scheduleTaskConfig.getScheduler());
        taskContext.setJdbcTemplate(scheduleTaskConfig.getJdbcTemplate());
        taskContext.setLock(this.lock);
        taskContext.setTaskClassName(this.taskClassName);
        taskContext.setStatus(this.status);
        taskContext.setTaskGroup(this.taskGroup);
        taskContext.setStartTime(this.startTime);
        taskContext.setTaskName(this.taskName);
    }

    private TaskStatus convert(String status) {
        if (TaskStatus.WAIT.getStatus().equals(status)) {
            return TaskStatus.WAIT;
        } else if (TaskStatus.COMPLETED.getStatus().equals(status)) {
            return TaskStatus.COMPLETED;
        } else if (TaskStatus.FAILED.getStatus().equals(status)) {
            return TaskStatus.FAILED;
        } else if (TaskStatus.RUNNING.getStatus().equals(status)) {
            return TaskStatus.RUNNING;
        }
        return null;
    }

    @Override
    public void setBeanName(String name) {
        this.taskName = name;
    }

    private TaskEntity query(String sql) {
        try {
            return scheduleTaskConfig.getJdbcTemplate().queryForObject(
                    sql, new Object[]{this.taskName}, new RowMapper<TaskEntity>() {
                        @Override
                        public TaskEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                            TaskEntity taskEntity = new TaskEntity();
                            taskEntity.setTaskName(rs.getString(fields[0]));
                            taskEntity.setTaskGroup(rs.getString(fields[3]));
                            taskEntity.setStatus(rs.getString(fields[1]));
                            taskEntity.setScheduleName(rs.getString(fields[5]));
                            taskEntity.setLock(rs.getInt(fields[2]));
                            taskEntity.setTaskClassName(rs.getString(fields[4]));
                            taskEntity.setStartTime(rs.getDate(fields[6]));
                            taskEntity.setEndTime(rs.getDate(fields[7]));
                            return taskEntity;
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private TaskEntity queryForObject() {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", join(fields), tablePrefix + TABLE_NAME, fields[0]);
        return query(sql);
    }

    private TaskEntity lockRow() {
        String lockSql = String.format("SELECT * FROM %s WHERE %s = ? FOR UPDATE", tablePrefix + TABLE_NAME, fields[0]);
        return query(lockSql);
    }

    private boolean insert() {
        String insertSql = String.format("INSERT INTO %s (%s) VALUES (?,?,?,?,?,?,?,?)", tablePrefix + TABLE_NAME, join(fields));
        return scheduleTaskConfig.getJdbcTemplate().update(insertSql, taskName, status.getStatus(), lock, taskGroup, taskClassName, scheduleName, null, null) == 1;
    }

    private boolean updateGuardStatus(int status, String timeField) {
        String updateSql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", tablePrefix + TABLE_NAME, fields[2], timeField, fields[0]);
        return scheduleTaskConfig.getJdbcTemplate().update(updateSql, status, new Date(), taskName) == 1;
    }

    private boolean validation() {
        String updateSql = String.format("UPDATE %s SET %s = ?", tablePrefix + TABLE_NAME, fields[2]);

        return scheduleTaskConfig.getJdbcTemplate().update(updateSql, EntranceGuard.RELEASE.getStatus()) == 1;
    }

    private String join(String[] array) {
        String result = "";
        for (String str : array) {
            result += str + ",";
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : result;
    }


    public void setScheduleTaskConfig(ScheduleTaskConfig scheduleTaskConfig) {
        this.scheduleTaskConfig = scheduleTaskConfig;
    }
}
