package sun.spring.scheduler.batch.processor;

/**
 * Created by root on 2016/3/8.
 */
public interface RedisKeyBuilder {

    String build(Object item);

}
