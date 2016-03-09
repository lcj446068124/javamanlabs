package sun.spring.scheduler.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import sun.spring.scheduler.domain.JobEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunyamorn on 3/6/16.
 * <p/>
 * Base class for all tasks.
 */
public abstract class AbstractScheduleJobDispatcher implements ScheduleJobDispatcher, BeanNameAware, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractScheduleJobDispatcher.class);

    protected List<ScheduleTask> scheduleTasks;

    private String jobName;

    protected ScheduleJobConfig scheduleJobConfig;

    private ScheduleJobContext scheduleJobContext;

    private ThreadPoolTaskScheduler executorService;

    public AbstractScheduleJobDispatcher() {
        scheduleJobContext = new ScheduleJobContext();
    }

    @Override
    public void run() {
        // synchronization lock of cluster server
        if (!tryLock())
            return;

        beforeJob();

        boolean hasException = false;
        try {
            // single thread task
            if (scheduleTasks.size() == 1) {
                ScheduleTask scheduleTask = scheduleTasks.get(0);
                scheduleTask.setScheduleJobContext(scheduleJobContext);
                scheduleTask.run();
            } else {
                for (ScheduleTask scheduleTask : scheduleTasks) {
                    scheduleTask.setScheduleJobContext(scheduleJobContext);
                    this.executorService.submit(scheduleTask);
                }
            }
        } catch (Exception e) {
            hasException = true;
            logger.error(e.getMessage());
        } finally {
            if (hasException) {
                scheduleJobContext.setStatus(JobStatus.FAILED);
            }else{
                scheduleJobContext.setStatus(JobStatus.COMPLETED);
            }
            afterJob();

            release();
        }
    }

    @Override
    public void afterJob() {
        scheduleJobContext.setEndTime(new Date());
        scheduleJobContext.setLock(EntranceGuard.RELEASE.getStatus());
        // persist status
        scheduleJobConfig.updateJobTimeAndStatus(scheduleJobContext.getStatus().getStatus(),
                ScheduleJobConfig.jobControlFields[7], jobName);
    }

    @Override
    public void beforeJob() {
        scheduleJobContext.setStartTime(new Date());
        scheduleJobContext.setLock(EntranceGuard.LOCK.getStatus());
        scheduleJobContext.setStatus(JobStatus.RUNNING);

        // persist status
        scheduleJobConfig.updateJobTimeAndStatus(scheduleJobContext.getStatus().getStatus(),
                ScheduleJobConfig.jobControlFields[6], jobName);
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
                    JobEntity entity = scheduleJobConfig.lockJobRow(jobName);
                    if (entity != null && entity.getLock() == EntranceGuard.RELEASE.getStatus()) {
                        if (!scheduleJobConfig.updateJobGuardStatus(EntranceGuard.LOCK.getStatus(), ScheduleJobConfig.jobControlFields[6], jobName)) {
                            logger.error("Update job guard status failed.");
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
                    if (!scheduleJobConfig.updateJobGuardStatus(EntranceGuard.RELEASE.getStatus(), ScheduleJobConfig.jobControlFields[7], jobName)) {
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
        assert (scheduleJobConfig != null);
        assert scheduleTasks != null && scheduleTasks.size() > 0;

        // init
        JobEntity entity = scheduleJobConfig.queryJob(jobName);
        scheduleJobContext.setScheduleJobConfig(scheduleJobConfig);
        scheduleJobContext.setJobName(jobName);

        if (entity == null) {
            // init job context
            scheduleJobContext.setStatus(JobStatus.WAIT);
            scheduleJobContext.setJobGroup(Thread.currentThread().getThreadGroup().getName());
            scheduleJobContext.setJobClassName(this.getClass().getName());
            scheduleJobContext.setLock(EntranceGuard.RELEASE.getStatus());
            scheduleJobContext.setScheduleName(scheduleJobConfig.getExecutorService().toString());
            if (!scheduleJobConfig.insertJob(jobName, scheduleJobContext.getStatus(),
                    scheduleJobContext.getLock(), scheduleJobContext.getJobGroup(),
                    scheduleJobContext.getJobClassName(), scheduleJobContext.getScheduleName())) {

                logger.error("Initialize job record failed.");
            }
        } else {
            scheduleJobContext.setStatus(convert(entity.getStatus()));
            scheduleJobContext.setJobClassName(entity.getJobClassName());
            scheduleJobContext.setLock(entity.getLock());
            scheduleJobContext.setScheduleName(entity.getJobName());
            scheduleJobContext.setJobGroup(entity.getJobGroup());
            scheduleJobContext.setStartTime(entity.getStartTime());
            scheduleJobContext.setEndTime(entity.getEndTime());
            // Validate run status and change all run status as wait status.
            scheduleJobConfig.validationJob();
        }
    }

    @Override
    public void destroy() throws Exception {
        executorService.destroy();
    }

    private JobStatus convert(String status) {
        if (JobStatus.WAIT.getStatus().equals(status)) {
            return JobStatus.WAIT;
        } else if (JobStatus.COMPLETED.getStatus().equals(status)) {
            return JobStatus.COMPLETED;
        } else if (JobStatus.FAILED.getStatus().equals(status)) {
            return JobStatus.FAILED;
        } else if (JobStatus.RUNNING.getStatus().equals(status)) {
            return JobStatus.RUNNING;
        }
        return null;
    }

    @Override
    public void setBeanName(String name) {
        this.jobName = name;
    }


    public void setScheduleJobConfig(ScheduleJobConfig scheduleJobConfig) {
        this.scheduleJobConfig = scheduleJobConfig;
    }

    public void setExecutorService(ThreadPoolTaskScheduler executorService) {
        this.executorService = executorService;
    }
}
