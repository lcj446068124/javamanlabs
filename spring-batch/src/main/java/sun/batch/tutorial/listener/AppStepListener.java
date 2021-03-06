package sun.batch.tutorial.listener;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 * Created by root on 2016/3/4.
 */
@Component
public class AppStepListener<T,S> extends StepListenerSupport<T,S> {
    @Override
    public void afterRead(T item) {
        System.out.println("after Read item");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        super.afterChunk(context);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("before Step=====");
        System.out.println("readCount=" + stepExecution.getReadCount());
        stepExecution.getExecutionContext().put("total",200);
    }
}
