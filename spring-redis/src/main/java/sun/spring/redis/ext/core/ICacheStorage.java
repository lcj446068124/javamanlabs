package sun.spring.redis.ext.core;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by root on 2015/10/28.
 */
public interface ICacheStorage<K, V> extends InitializingBean, DisposableBean {

    Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    void set(K key, V value);

    /**
     * @param key
     * @param value
     * @param expire seconds
     * @return
     */
    void setEx(K key, V value, long expire);

    /**
     * Set key to hold string value if key does not exist. In that case, it is equal to
     * SET, When key already holds a value, no operation is performed. SETNX is short for
     * "SET if Not eXists".
     *
     * @param key
     * @param value
     */
    void setNx(K key, V value);

    /**
     * Sets the given keys to their respective values. MSET replaces existing values with
     * new values, just as regular SET.
     * MSET is atomic. so all given keys are set at once.
     *
     * @param values
     */
    void mSet(Map<K, V> values);

    /**
     * Sets the given keys to their respective values. MSETNX will not perform any operation
     * at all even if just a single key already exists.
     *
     * @param values
     */
    void mSetNX(Map<K, V> values);

    /**
     * Get the value of key.
     *
     * @param key
     * @return
     */
    V get(K key);

    /**
     * O(N) where N is the number of keys that will be removed.
     *
     * @param keys
     * @return The number of keys that were removed.
     */
    Long del(K... keys);

    /**
     * Return if key is exists. Since Redis 3.0.3 it is possible to specify multiple keys.
     * @param key
     * @return
     */
    Boolean exists(K key);

}

