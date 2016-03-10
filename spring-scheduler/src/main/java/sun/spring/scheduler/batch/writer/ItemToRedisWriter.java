package sun.spring.scheduler.batch.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import sun.spring.scheduler.batch.processor.RedisKeyBuilder;
import sun.spring.scheduler.batch.processor.Tuple;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 2016/3/3.
 */
public class ItemToRedisWriter implements ItemWriter<Tuple<String, Object>> {

    private RedisTemplate<String, Object> redisTemplate;

    private RedisKeyBuilder redisKeyBuilder;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRedisKeyBuilder(RedisKeyBuilder redisKeyBuilder) {
        this.redisKeyBuilder = redisKeyBuilder;
    }

    @Override
    public void write(List<? extends Tuple<String, Object>> items) throws Exception {
        final List<Tuple<String, String>> list = new LinkedList<>();

        for (Tuple<String, Object> tuple : items) {
            String json = tuple.getKey();
            Object value = tuple.getValue();
            String key = redisKeyBuilder.build(value);
            list.add(new Tuple<>(key, json));
        }

        // Use Pipeline
        redisTemplate.execute(new RedisCallback<Void>() {
            @SuppressWarnings("unchecked")
            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                for(Tuple<String,String> tuple : list){
                    RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                    RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                    connection.set(keySerializer.serialize(tuple.getKey()), valueSerializer.serialize(tuple.getValue()));
                }
                return null;
            }
        }, false, true);

    }
}
