package sun.spring.scheduler.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.util.List;

/**
 * Created by root on 2016/3/7.
 */
public class SimpleStepListener <T,S> extends StepListenerSupport<T,S> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleStepListener.class);

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

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if(stepExecution.getReadCount() == 0){
            return ExitStatus.NOOP ;
        }
        return super.afterStep(stepExecution);
    }
}
