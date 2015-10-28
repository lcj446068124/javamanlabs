package sun.spring.redis.ext.core;

import org.springframework.util.StringUtils;

/**
 * Created by root on 2015/10/28.
 */
public abstract class CacheSupport<V> extends AbstractBaseCache<V>
        implements CacheCallBack<String, V>, CacheKeyGenerator<String>{
    private ICacheStorage<String, V> storage;

    private int timeOut;

    private boolean enableTimeOut = false;

    public void setTimeOut(int timeOut){
        enableTimeOut = timeOut > 0;
        this.timeOut = timeOut;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        CacheManager.getInstance().registerCache(this);
    }


    @Override
    public String generate(String key) {
        return this.getContext().getCacheIdentify() + KEY_SEPERATOR + doGenerate(key);
    }

    @Override
    public String doGenerate(String key) {
        return key;
    }

    @Override
    public V get(CacheKeyBuilder keyBuilder) {
        String key = keyBuilder.getCacheKey();
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("Key not allowed is null or an empty string.");
        }

        V value = null;
        String keyFullName = generate(key);
        try {
            value = this.storage.get(keyFullName);
        }catch (Exception e){
            e.printStackTrace();
        }

        return value;
    }
}
