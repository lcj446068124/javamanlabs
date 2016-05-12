package sun.spring.scheduler.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import sun.spring.scheduler.dao.DataAccessOperation;
import sun.spring.scheduler.domain.Constant;
import sun.spring.scheduler.domain.JobEntity;

/**
 * Created by yamorn on 2016/5/12.
 */
public class ConfigService implements Services {

    private ThreadPoolTaskScheduler executorService;

    private DataAccessOperation<JobEntity, String> dataAccessOperation;

    @Override
    public ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
        return executorService;
    }

    @Override
    public DataAccessOperation<JobEntity, String> getDataAccessOperation() {
        return dataAccessOperation;
    }

    public ThreadPoolTaskScheduler getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ThreadPoolTaskScheduler executorService) {
        this.executorService = executorService;
    }

    public void setDataAccessOperation(DataAccessOperation<JobEntity, String> dataAccessOperation) {
        this.dataAccessOperation = dataAccessOperation;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Provider.getInstance().register(Constant.CONFIG_SERVICE_KEY, this);
    }
}
