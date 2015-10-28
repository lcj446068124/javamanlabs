package sun.spring.redis.ext.demo;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.spring.redis.ext.core.CacheSupport;

/**
 * Created by root on 2015/10/28.
 */
@Component
public class UserCache extends CacheSupport<User> {
    @Override
    public User doGet(String key) {
        return null;
    }

    @Override
    public String getCacheIdentify() {
        return "xx";
    }
}
