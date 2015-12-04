package sun.spring.redis.ext.core;

/**
 * Created by root on 2015/10/29.
 */
public interface DaoCallback<K, V> {

    V doGet(K key);

}
