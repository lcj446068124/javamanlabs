package sun.spring.scheduler.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import sun.spring.scheduler.core.AbstractScheduleTask;
import sun.spring.scheduler.core.TaskContext;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 2016/3/7.
 */
public class JobDispatcher extends AbstractScheduleTask implements Dispatcher {

    protected static final Logger logger = LoggerFactory.getLogger(JobDispatcher.class);

    private List<Event> events;

    @Override
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public void doTask(TaskContext taskContext) {
        dispatcher();
    }

    @Override
    public void dispatcher() {
        if (events == null || events.size() == 0)
            return;

        summary(events);

        for (Event event : events) {
            Trigger trigger = event.getTrigger();
            trigger.onFire();
        }
    }

    @Override
    public synchronized void summary(List<Event> events) {
        StringBuilder sb = new StringBuilder();
        if (events != null && events.size() > 0) {
            sb.append("Batch Dispatcher Summary:\n");
            sb.append("\tEvents number: ").append(events.size()).append("\n");
            for (Event event : events) {
                sb.append("\t\tEvent Name: ").append(event.getName());
                Trigger trigger = event.getTrigger();
                if (trigger != null) {
                    sb.append("\tTrigger Name: ").append(trigger.getName()).append("\n");
                    Map<Job, Guard> jobGuardMap = trigger.getJobs();
                    if (jobGuardMap != null && jobGuardMap.size() > 0) {
                        for (Map.Entry<Job, Guard> entry : jobGuardMap.entrySet()) {
                            sb.append("\t\t\tJob Name: ").append(entry.getKey().getName());
                            sb.append("\tGuard Name: ").append(entry.getValue().getName());
                            sb.append("\n");
                        }
                    }
                }
            }
            logger.debug(sb.toString());
        }
    }
}
