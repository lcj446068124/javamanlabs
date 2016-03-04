package sun.batch.tutorial.listener;

import org.springframework.batch.core.listener.StepListenerSupport;
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
}
