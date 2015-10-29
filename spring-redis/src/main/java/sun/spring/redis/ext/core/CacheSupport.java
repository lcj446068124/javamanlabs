package sun.spring.redis.ext.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by root on 2015/10/28.
 */
public abstract class CacheSupport<V> extends AbstractBaseCache<V>
        implements DaoCallback<CacheKey, V>, CacheKeyGenerator<CacheKey> {

    private int timeOut;

    private boolean enableTimeOut = false;

    @Autowired
    protected ICacheStorage<String, V> cacheStorage;

    public void setTimeOut(int timeOut) {
        enableTimeOut = timeOut > 0;
        this.timeOut = timeOut;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        CacheManager.getInstance().registerCache(this);
    }

    @Override
    public String generate(CacheKey key) {
        return this.getContext().getCacheIdentify() + CacheKeyGenerator.KEY_CONTEXT_SEPARATOR + key.getCacheKey();
    }

    @Override
    public V get(CacheKey cacheKey) {
        String key = cacheKey.getCacheKey();
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("Key not allowed is null or an empty string.");
        }

        V value = null;
        String keyFullName = generate(cacheKey);
        try {
//            value = this.getCacheStorage().get(keyFullName);
            value = cacheStorage.get(keyFullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
