package sun.spring.redis.ext.core;

import java.util.Map;

/**
 * Created by root on 2015/10/28.
 */
public interface ICacheStorage<K, V> {

    boolean set(K key, V value);

    /**
     * @param key
     * @param value
     * @param expire seconds
     * @return
     */
    boolean setEx(K key, V value, long expire);

    boolean setNx(K key, V value);

    boolean mSet(Map<K, V> values);

    boolean mSetEx(Map<K, V> values, long expire);

    V get(K key);

    void del(K... key);

}

