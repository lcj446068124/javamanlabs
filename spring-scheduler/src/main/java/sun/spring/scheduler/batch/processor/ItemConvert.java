package sun.spring.scheduler.batch.processor;

/**
 * Created by root on 2016/3/8.
 */
public interface ItemConvert<V,T> {

    V convert(T item);

}
