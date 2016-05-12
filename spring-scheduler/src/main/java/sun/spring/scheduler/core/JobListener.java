package sun.spring.scheduler.core;

/**
 * Created by yamorn on 2016/5/12.
 */
public interface JobListener {

    void jobStart(JobContext context);

    void jobDone(JobContext context);

}
