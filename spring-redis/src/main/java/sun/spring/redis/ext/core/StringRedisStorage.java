package sun.spring.redis.ext.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by root on 2015/10/28.
 */
@Component
public class StringRedisStorage<V> implements ICacheStorage<String, V> {

    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    @Override
    public boolean set(String key, V value) {
        return false;
    }

    @Override
    public boolean setEx(String key, V value, long expire) {
        return false;
    }

    @Override
    public boolean setNx(String key, V value) {
        return false;
    }

    @Override
    public boolean mSet(Map<String, V> values) {
        return false;
    }

    @Override
    public boolean mSetEx(Map<String, V> values, long expire) {
        return false;
    }

    @Override
    public V get(String key) {
        return null;
    }

    @Override
    public void del(String... key) {

    }
}
