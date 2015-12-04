package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/28.
 */
public interface CacheKeyGenerator<K> {

    String KEY_CONTEXT_SEPARATOR = "_";

    String KEY_VALUE_PAIRS_SEPARATOR = ";";

    String KEY_VALUE_SEPARATOR = ":";

    String generate(K key);
}
