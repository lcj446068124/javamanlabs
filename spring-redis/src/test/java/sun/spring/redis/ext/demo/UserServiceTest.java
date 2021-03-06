package sun.spring.redis.ext.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by root on 2015/10/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:application-context.xml"})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void testGetUser() throws Exception {
        User user = userService.getUser("xxxx");
        System.out.println(user);
    }

    @Test
    public void testRedisTemplate(){
        redisTemplate.opsForHash().put("mmhash","hello","world");
    }

    @Test
    public void testOp(){
        String value=(String)redisTemplate.opsForHash().get("mhash", "hello");
        System.out.println(value);
    }
}
