package sun.spring.scheduler.dao;

/**
 * Created by yamorn on 2016/5/12.
 */
public interface OperationCallback<T, V> {
    V process(T t);
}
