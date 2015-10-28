package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/28.
 */
public interface CacheKeyGenerator<K> {

    public static final String KEY_SEPERATOR = "_";

    K generate(K key);
}
