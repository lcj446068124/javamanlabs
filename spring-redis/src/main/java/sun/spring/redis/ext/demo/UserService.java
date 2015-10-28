package sun.spring.redis.ext.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sun.spring.redis.ext.core.ICache;
import sun.spring.redis.ext.core.KeyValuePairsCacheBuilder;

import javax.annotation.Resource;


/**
 * Created by root on 2015/10/28.
 */
@Service
public class UserService {

    @Autowired
    private ICache<User> userCache;

    public User getUser(String userName){
        return userCache.get(new KeyValuePairsCacheBuilder().put("userName",userName).build());
    }
}
