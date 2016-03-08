package sun.spring.scheduler.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.StepListenerSupport;

import java.util.List;

/**
 * Created by root on 2016/3/7.
 */
public class StepErrorListener<T, S> extends StepListenerSupport<T, S> {

    private static final Logger logger = LoggerFactory.getLogger(StepErrorListener.class);

    @Override
    public void onReadError(Exception ex) {
        logger.error("Encountered error on read", ex);
    }

    @Override
    public void onProcessError(T item, Exception e) {
        logger.error("Encountered error on process", e);
    }

    @Override
    public void onWriteError(Exception exception, List<? extends S> items) {
        logger.error("Encountered error on write", exception);
    }


}
