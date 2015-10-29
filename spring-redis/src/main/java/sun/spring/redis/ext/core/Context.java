package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/28.
 */
public class Context {

    private ICache<?, ?> cache;

    public Context(ICache<?, ?> cache) {
        this.cache = cache;
    }

    public String getCacheIdentify() {
        return this.cache.getCacheIdentify();
    }
}
