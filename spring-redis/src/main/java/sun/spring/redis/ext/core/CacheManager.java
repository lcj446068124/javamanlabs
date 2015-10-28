package sun.spring.redis.ext.core;

import sun.spring.redis.ext.exception.CacheDumplicateException;
import sun.spring.redis.ext.exception.CacheNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 2015/10/28.
 */
public class CacheManager {
    private static final CacheManager instance = new CacheManager();

    private final Map<String, ICache> caches = new ConcurrentHashMap<String, ICache>();

    private CacheManager() {
    }

    public static CacheManager getInstance() {
        return instance;
    }

    public <K extends String, V> void registerCache(ICache<V> cache) {
        String uniqueCacheKeyPrefix = cache.getCacheIdentify();
        if (caches.containsKey(uniqueCacheKeyPrefix)) {
            throw new CacheDumplicateException(
                    "CacheManager-registerCache：" + uniqueCacheKeyPrefix
                            + " to Class:[" + cache.getClass().getName()
                            + " and Class: "
                            + caches.get(uniqueCacheKeyPrefix).getClass().getName()
                            + " duplicated.");
        }
        caches.put(uniqueCacheKeyPrefix, cache);
    }

    @SuppressWarnings("unchecked")
    public <V> ICache<V> getCache(String cacheIdentify) {
        ICache<V> cache = caches.get(cacheIdentify);
        if (cache == null) {
            throw new CacheNotFoundException("CacheIdentify: " + cacheIdentify + " not found.");
        }
        return cache;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCache(Class t, String cacheIdentify) {
        try {
            return (T) getCache(cacheIdentify);
        } catch (ClassCastException e) {
            throw new CacheNotFoundException("CacheId: " + cacheIdentify + " to Class: " + t.getName() + " not found");
        }
    }

    public void destroy() {
        caches.clear();
    }


}
