package sun.spring.scheduler.batch;

import java.util.List;

/**
 * Created by root on 2016/3/7.
 */
public interface Dispatcher {

    void dispatcher();

    void setEvents(List<Event> events);

    void summary(List<Event> events);


}
