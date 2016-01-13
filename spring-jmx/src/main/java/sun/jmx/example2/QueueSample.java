package sun.jmx.example2;

import java.beans.ConstructorProperties;
import java.util.Date;

/**
 * Created by root on 2016/1/13.
 * <p/>
 * Java type representing a snapshot of a given queue.
 * It bundles together the instant time the snapshot was taken, the queue
 * size and the queue head.
 */
public class QueueSample {
    private final Date date;
    private final int size;
    private final String head;

    @ConstructorProperties({"date", "size", "head"})
    public QueueSample(Date date, int size, String head) {
        this.date = date;
        this.size = size;
        this.head = head;
    }

    public Date getDate() {
        return date;
    }

    public int getSize() {
        return size;
    }

    public String getHead() {
        return head;
    }
}
