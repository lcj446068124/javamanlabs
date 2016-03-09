package sun.spring.scheduler.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import sun.spring.scheduler.domain.TaskEntity;

import java.util.Date;

/**
 * Created by root on 2016/3/9.
 */
public abstract class AbstractScheduleTask implements ScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(AbstractScheduleTask.class);

    private ScheduleJobContext scheduleJobContext;

    private ScheduleTaskContext scheduleTaskContext;

    protected ScheduleTaskDetail scheduleTaskDetail;

    private ScheduleJobConfig scheduleJobConfig;

    protected String name;

    public AbstractScheduleTask() {
        scheduleTaskContext = new ScheduleTaskContext();
    }

    public void setScheduleJobConfig(ScheduleJobConfig scheduleJobConfig) {
        this.scheduleJobConfig = scheduleJobConfig;
    }

    @Override
    public void beforeTask() {
        scheduleTaskContext.setStartTime(new Date());
        scheduleTaskContext.setLock(EntranceGuard.LOCK.getStatus());
        scheduleTaskContext.setStatus(TaskStatus.RUNNING);
        // persist status
        scheduleJobConfig.updateTaskTimeAndStatus(scheduleTaskContext.getStatus().getStatus(),
                ScheduleJobConfig.taskControlFields[6], name);
    }

    @Override
    public void afterTask() {
        scheduleTaskContext.setEndTime(new Date());
        scheduleTaskContext.setLock(EntranceGuard.RELEASE.getStatus());
        // persist status
        scheduleJobConfig.updateTaskTimeAndStatus(scheduleTaskContext.getStatus().getStatus(),
                ScheduleJobConfig.taskControlFields[7], name);
    }

    public abstract void setScheduleTaskDetail(ScheduleTaskDetail scheduleTaskDetail);

    @Override
    public void run() {

        boolean result = false;
        try {
            result = scheduleTaskDetail.precondition();
        } catch (Exception e) {
            // ignore
        }

        if (result) {
            if (tryLock()) {

                beforeTask();

                boolean hasException = false;
                try {
                    scheduleTaskDetail.doTask(scheduleTaskContext);
                } catch (Exception e) {
                    hasException = true;
                    logger.error(e.getMessage());
                } finally {
                    if (hasException) {
                        scheduleTaskContext.setStatus(TaskStatus.FAILED);
                    }else{
                        scheduleTaskContext.setStatus(TaskStatus.COMPLETED);
                    }
                    afterTask();

                    release();
                }
            }
        }
    }

    @Override
    public void setScheduleJobContext(ScheduleJobContext scheduleJobContext) {
        this.scheduleJobContext = scheduleJobContext;
    }

    @Override
    public ScheduleTaskContext getTaskContext() {
        return scheduleTaskContext;
    }

    private boolean tryLock() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(scheduleJobConfig.getTransactionManager());
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                boolean result = true;
                try {
                    TaskEntity entity = scheduleJobConfig.lockTaskRow(name);
                    if (entity != null && entity.getLock() == EntranceGuard.RELEASE.getStatus()) {
                        if (!scheduleJobConfig.updateTaskGuardStatus(EntranceGuard.LOCK.getStatus(),
                                ScheduleJobConfig.taskControlFields[6], name)) {
                            logger.error("Update task guard status failed.");
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
        TransactionTemplate transactionTemplate = new TransactionTemplate(scheduleJobConfig.getTransactionManager());
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                boolean result = true;
                try {
                    if (!scheduleJobConfig.updateTaskGuardStatus(EntranceGuard.RELEASE.getStatus(), ScheduleJobConfig.taskControlFields[7], name)) {
                        logger.error("Release task row lock failed.");
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
    public void setBeanName(String name) {
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert scheduleTaskDetail != null;

        // init
        TaskEntity entity = scheduleJobConfig.queryTask(name);

        scheduleTaskContext.setTaskName(name);
        if (entity == null) {
            // init task context
            scheduleTaskContext.setStatus(TaskStatus.WAIT);
            scheduleTaskContext.setTaskGroup(Thread.currentThread().getThreadGroup().getName());
            scheduleTaskContext.setTaskClassName(this.getClass().getName());
            scheduleTaskContext.setLock(EntranceGuard.RELEASE.getStatus());
            scheduleTaskContext.setScheduleName(scheduleJobConfig.getExecutorService().toString());
            if (!scheduleJobConfig.insertTask(name, scheduleTaskContext.getStatus(),
                    scheduleTaskContext.getLock(), scheduleTaskContext.getTaskGroup(),
                    scheduleTaskContext.getTaskClassName(), scheduleTaskContext.getScheduleName())) {
                logger.error("Initialize task record failed.");
            }
        } else {
            scheduleTaskContext.setStatus(convert(entity.getStatus()));
            scheduleTaskContext.setTaskClassName(entity.getTaskClassName());
            scheduleTaskContext.setLock(entity.getLock());
            scheduleTaskContext.setScheduleName(entity.getTaskName());
            scheduleTaskContext.setTaskGroup(entity.getTaskGroup());
            scheduleTaskContext.setStartTime(entity.getStartTime());
            scheduleTaskContext.setEndTime(entity.getEndTime());
            // Validate run status and change all run status as wait status.
            scheduleJobConfig.validationTask();
        }
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
}
