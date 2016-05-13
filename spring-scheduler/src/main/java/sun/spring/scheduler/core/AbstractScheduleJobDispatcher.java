package sun.spring.scheduler.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import sun.spring.scheduler.dao.DataAccessOperation;
import sun.spring.scheduler.dao.OperationCallback;
import sun.spring.scheduler.domain.Constant;
import sun.spring.scheduler.domain.JobEntity;
import sun.spring.scheduler.service.Provider;
import sun.spring.scheduler.service.Services;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by sunyamorn on 3/6/16.
 * <p/>
 * Base class for all tasks.
 */
public abstract class AbstractScheduleJobDispatcher implements ScheduleJobDispatcher, BeanNameAware,
        InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractScheduleJobDispatcher.class);

    private String jobName;

    private JobContext jobContext;

    private JobListener jobListener;

    private ThreadPoolTaskScheduler executorService;

    protected List<ScheduleTask> scheduleTasks;

    private DataAccessOperation<JobEntity, String> dataAccessOperation;

    public AbstractScheduleJobDispatcher() {
        jobContext = new JobContext();
    }


    @Override
    public void run() {
        // synchronization lock of distribute servers
        if (!tryLock())
            return;

        // update job status & job context
        beforeJob();

        // job launch listener to record log
        if (jobListener != null) {
            jobListener.jobStart(jobContext);
        }

        Map<String, Object> retVal = new HashMap<>();

        boolean hasException = false;
        try {
            // single thread task
            if (scheduleTasks.size() == 1) {
                ScheduleTask scheduleTask = scheduleTasks.get(0);
                scheduleTask.setJobContext(jobContext);
                retVal = scheduleTask.call();
            } else {
                Stack<Future<Map<String, Object>>> stack = new Stack<>();
                for (ScheduleTask scheduleTask : scheduleTasks) {
                    scheduleTask.setJobContext(jobContext);
                    Future<Map<String, Object>> future = Provider.getInstance()
                            .get(Constant.CONFIG_SERVICE_KEY).getThreadPoolTaskScheduler().submit(scheduleTask);
                    stack.push(future);
                }
                // wait for all tasks completed
                while (!stack.isEmpty()) {
                    Map<String, Object> taskRetVal = stack.pop().get(); // get() will block thread
                    if (taskRetVal != null) {
                        retVal.putAll(taskRetVal);
                    }
                }
            }
            // add all task result to job context;
            jobContext.setJobRetVal(retVal);
        } catch (Exception e) {
            hasException = true;
            jobContext.setException(e);
            logger.error(e.getMessage());
        } finally {
            if (hasException) {
                jobContext.setScheduleStatus(ScheduleStatus.FAILED);
            } else {
                jobContext.setScheduleStatus(ScheduleStatus.COMPLETED);
            }

            // update job context & reset job status
            afterJob(hasException);

            // release job lock
            release();

            // record log
            if (jobListener != null) {
                jobListener.jobDone(jobContext);
            }
        }
    }


    @Override
    public void beforeJob() {
        updateJobContext(new Date(), false, ScheduleStatus.RUNNING);
    }

    @Override
    public void afterJob(boolean hasException) {
        updateJobContext(new Date(), false, hasException ? ScheduleStatus.FAILED : ScheduleStatus.COMPLETED);
    }

    private boolean tryLock() {
        return dataAccessOperation.pessimisticLockQuery(jobName, new OperationCallback<JobEntity, Boolean>() {
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
    }

    private void updateJobContext(Date date, boolean isStart, ScheduleStatus scheduleStatus) {
        // maintain context in memory
        if (isStart) {
            jobContext.setStartTime(date).setJobLock(EntranceGuard.LOCK.getStatus());
        } else {
            jobContext.setEndTime(date).setJobLock(EntranceGuard.RELEASE.getStatus());
        }
        jobContext.setScheduleStatus(scheduleStatus);

        // maintain context in db
        dataAccessOperation.updateJobStatus(jobName, scheduleStatus);
    }

    private boolean release() {
        return dataAccessOperation.updateLockStatus(jobName, EntranceGuard.RELEASE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        // This attribute depends on ConfigService class
        Services services = Provider.getInstance().get(Constant.CONFIG_SERVICE_KEY);
        executorService = services.getThreadPoolTaskScheduler();
        dataAccessOperation = services.getDataAccessOperation();

        assert (dataAccessOperation != null);
        assert scheduleTasks != null && scheduleTasks.size() > 0;

        // init
        JobEntity entity = dataAccessOperation.query(jobName, false);
        jobContext.setJobName(jobName)
                .setScheduleStatus(ScheduleStatus.WAIT)
                .setJobGroup(Thread.currentThread().getThreadGroup().getName())
                .setJobClassName(this.getClass().getName())
                .setJobLock(EntranceGuard.RELEASE.getStatus())
                .setScheduleName(executorService.getClass().getName());

        if (entity == null) {
            // persist job
            entity = new JobEntity();
            entity.setJobName(jobName);
            entity.setJobStatus(jobContext.getScheduleStatus().getStatus());
            entity.setJobGroup(jobContext.getJobGroup());
            entity.setJobClassName(jobContext.getJobClassName());
            entity.setJobLock(jobContext.getJobLock());
            entity.setScheduleName(jobContext.getScheduleName());

            if (!dataAccessOperation.insert(entity))
                logger.error("Initialize job record failed.");
        }
    }

    @Override
    public void destroy() throws Exception {
        executorService.destroy();
    }

    @Override
    public void setBeanName(String name) {
        this.jobName = name;
    }

    @Override
    public void setScheduleTasks(List<ScheduleTask> scheduleTasks) {
        this.scheduleTasks = scheduleTasks;
    }

    @Override
    public void setJobListener(JobListener jobListener) {
        this.jobListener = jobListener;
    }

}
