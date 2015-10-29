package sun.spring.redis.ext.core;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * Created by root on 2015/10/29.
 */
public class StringCacheStorage<V> implements ICacheStorage<String, V> {

    private RedisTemplate<String, V> redisTemplate;

    public RedisTemplate<String, V> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void destroy() throws Exception {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.setValueSerializer(new ExtJsonSerializer());
    }

    @Override
    public void set(final String key, final V value) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key.getBytes(DEFAULT_CHARSET), value.toString().getBytes(DEFAULT_CHARSET));
                return null;
            }
        });
    }

    @Override
    public void setEx(final String key, final V value, final long expire) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(key.getBytes(DEFAULT_CHARSET), expire, value.toString().getBytes(DEFAULT_CHARSET));
                return null;
            }
        });
    }

    @Override
    public void setNx(final String key, final V value) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setNX(key.getBytes(DEFAULT_CHARSET), value.toString().getBytes(DEFAULT_CHARSET));
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void mSet(final Map<String, V> values) {
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Map.Entry<String, V> entry : values.entrySet()) {
                    String key = entry.getKey();
                    V value = entry.getValue();
                    connection.set(key.getBytes(DEFAULT_CHARSET), value.toString().getBytes(DEFAULT_CHARSET));
                }
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void mSetNX(final Map<String, V> values) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Map.Entry<String, V> entry : values.entrySet()) {
                    String key = entry.getKey();
                    V value = entry.getValue();
                    connection.setNX(key.getBytes(DEFAULT_CHARSET), value.toString().getBytes(DEFAULT_CHARSET));
                }
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(final String key) {
        String value = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
               byte[] result = connection.get(key.getBytes(DEFAULT_CHARSET));
                if(result != null){
                    return new String(result, DEFAULT_CHARSET);
                }
                return null;
            }
        });
        if (value != null) {
            return (V) this.redisTemplate.getValueSerializer().deserialize(value.getBytes(DEFAULT_CHARSET));
        }
        return null;
    }

    @Override
    public Long del(final String... keys) {
        final long num = 0;
        redisTemplate.executePipelined(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long num = 0;
                for (String key : keys) {
                    num += connection.del(key.getBytes(DEFAULT_CHARSET));
                }
                return num;
            }
        });
        return num;
    }

    @Override
    public Boolean exists(final String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes(DEFAULT_CHARSET));
            }
        });
    }
}
