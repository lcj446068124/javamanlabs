package sun.spring.scheduler.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import sun.spring.scheduler.dao.DataAccessOperation;

/**
 * Created by yamorn on 2016/5/12.
 */
public interface Services extends InitializingBean{

    ThreadPoolTaskScheduler getThreadPoolTaskScheduler();

    DataAccessOperation getDataAccessOperation();


}
