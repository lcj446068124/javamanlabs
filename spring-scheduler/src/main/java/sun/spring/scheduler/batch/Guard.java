package sun.spring.scheduler.batch;

import org.springframework.beans.factory.BeanNameAware;

/**
 * Created by root on 2016/3/7.
 */
public interface Guard extends BeanNameAware {

    void lock();

    void release();

    String getName();

}
