package sun.spring.scheduler.batch;

import org.springframework.beans.factory.BeanNameAware;

/**
 * Created by root on 2016/3/7.
 */
public interface Event extends BeanNameAware{

    Trigger getTrigger();

    void setTrigger(Trigger trigger);

    String getName();

}
