package sun.spring.scheduler.demo;

import sun.spring.scheduler.batch.AbstractEvent;
import sun.spring.scheduler.batch.Event;
import sun.spring.scheduler.batch.Trigger;

/**
 * Created by root on 2016/3/7.
 */
public class DemoEvent extends AbstractEvent {
    private Trigger trigger;

    @Override
    public Trigger getTrigger() {
        return trigger;
    }

    @Override
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
