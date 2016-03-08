package sun.spring.scheduler.batch.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sun.spring.scheduler.batch.processor.RedisKeyBuilder;
import sun.spring.scheduler.batch.processor.Tuple;

import java.util.List;

/**
 * Created by root on 2016/3/3.
 */
public class ItemToRedisWriter<T> implements ItemWriter<T> {

    private RedisTemplate<String, Object> redisTemplate;

    private RedisKeyBuilder redisKeyBuilder;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRedisKeyBuilder(RedisKeyBuilder redisKeyBuilder) {
        this.redisKeyBuilder = redisKeyBuilder;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        for(T t : items){
            if (t instanceof Tuple) {
                Tuple<String,Object> tuple = (Tuple<String,Object>)t;
                String json = tuple.getKey();
                Object value = tuple.getValue();
                System.out.println("writer:"+ json);
//                String key = redisKeyBuilder.build(value);
//                redisTemplate.opsForValue().set(key, json);
            }
        }
    }
}
