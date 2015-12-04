package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/29.
 */
public interface CacheKey<T> {

    String getCacheKey();

    T parseCacheKey();
}
