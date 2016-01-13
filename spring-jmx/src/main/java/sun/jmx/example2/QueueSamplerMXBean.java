package sun.jmx.example2;

/**
 * Created by root on 2016/1/13.
 * <p/>
 * MXBean interface describing the management
 * operations and attributes for the QueueSampler MXBean. In this case
 * there is a read-only attribute "QueueSample" and an operation "clearQueue".
 */
public interface QueueSamplerMXBean {

    QueueSample getQueueSample();

    void clearQueue();
}
