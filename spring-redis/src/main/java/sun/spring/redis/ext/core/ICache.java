package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/28.
 */
public interface ICache<V, T> {

    String getCacheIdentify();

    V get(CacheKey<T> cacheKey);


}
