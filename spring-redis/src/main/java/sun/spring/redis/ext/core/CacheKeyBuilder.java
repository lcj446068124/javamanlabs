package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/28.
 */
public interface CacheKeyBuilder {

    public CacheKeyBuilder build();

    public String getCacheKey();

}
