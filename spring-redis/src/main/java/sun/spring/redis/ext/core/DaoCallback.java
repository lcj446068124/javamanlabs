package sun.spring.redis.ext.core;

/**
 * Created by sunyamorn on 1/13/16.
 */
public interface DaoCallback<K, V> {
    V doGet(K key);
}
