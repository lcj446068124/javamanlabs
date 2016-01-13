package sun.jmx.example2;

import java.util.Date;
import java.util.Queue;

/**
 * Created by root on 2016/1/13.
 * <p/>
 * MXBean implementation for the QueueSampler MXBean.
 * This class must implement all the Java methods declared in the
 * QueueSamplerMXBean interface, with the appropriate behavior for each one.
 */
public class QueueSampler implements QueueSamplerMXBean {

    private Queue<String> queue;

    public QueueSampler(Queue<String> queue) {
        this.queue = queue;
    }

    @Override
    public QueueSample getQueueSample() {
        synchronized (queue) {
            return new QueueSample(new Date(), queue.size(), queue.peek());
        }
    }

    @Override
    public void clearQueue() {
        synchronized (queue) {
            queue.clear();
        }
    }
}
