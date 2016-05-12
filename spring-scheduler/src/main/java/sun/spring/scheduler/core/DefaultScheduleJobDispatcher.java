package sun.spring.scheduler.core;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Created by root on 2016/3/9.
 *
 * This bean's scope must be prototype
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultScheduleJobDispatcher extends AbstractScheduleJobDispatcher {
    @Override
    public void setScheduleTasks(List<ScheduleTask> scheduleTasks) {
        this.scheduleTasks = scheduleTasks;
    }
}
