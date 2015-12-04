package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/28.
 */
public interface CacheCallBack<K, V> {

    V doGet(K key);

    K doGenerate(K key);
}
