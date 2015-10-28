package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/28.
 */
public abstract class AbstractCacheKeyBuilder implements CacheKeyBuilder{
    @Override
    public CacheKeyBuilder build() {
        return this;
    }
}
