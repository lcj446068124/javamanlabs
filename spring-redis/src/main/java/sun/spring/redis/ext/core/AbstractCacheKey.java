package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/29.
 */
public abstract class AbstractCacheKey<T> implements CacheKey<T>{

    protected String cacheKey;

    public AbstractCacheKey(String cacheKey){
        this.cacheKey = cacheKey;
    }

    public AbstractCacheKey(){}

}
