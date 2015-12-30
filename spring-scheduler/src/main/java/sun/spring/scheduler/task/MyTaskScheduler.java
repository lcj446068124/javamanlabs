package sun.spring.scheduler.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * Created by 264929 on 2015/12/4.
 */
@Component
public class MyTaskScheduler {
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    public void test(){
    }
}
