package sun.spring.redis.ext.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by root on 2015/10/28.
 */
public abstract class CacheSupport<V, T> extends AbstractBaseCache<V, T>
        implements DaoCallback<CacheKey<T>, V>, CacheKeyGenerator<CacheKey<T>> {

    private static final Logger logger = LoggerFactory.getLogger(CacheSupport.class);

    private long timeOut;

    private boolean enableTimeOut = false;

    @Autowired
    protected ICacheStorage<String, V> cacheStorage;

    public void setTimeOut(int timeOut) {
        this.enableTimeOut = timeOut > 0;
        this.timeOut = timeOut;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        CacheManager.getInstance().registerCache(this);
    }

    @Override
    public String generate(CacheKey<T> key) {
        return this.getContext().getCacheIdentify() + CacheKeyGenerator.KEY_CONTEXT_SEPARATOR + key.getCacheKey();
    }

    @Override
    public V get(CacheKey<T> cacheKey) {
        String key = cacheKey.getCacheKey();
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("Key not allowed is null or an empty string.");
        }

        V value = null;
        String keyFullName = generate(cacheKey);
        try {
            value = this.cacheStorage.get(keyFullName);
            if(value == null){
                // Key exists, but value is null. return directly.
                if(this.cacheStorage.exists(keyFullName)){
                    return null;
                }
                // Key not exists. Get data from database and put value into cache.
                value = doGet(cacheKey);
                if(value != null){
                    if(this.enableTimeOut){
                        this.cacheStorage.setEx(keyFullName, value, this.timeOut);
                    }else{
                        this.cacheStorage.set(keyFullName, value);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return value;
    }
}
