package sun.demo.mybatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.demo.mybatis.dao.UserDao;
import sun.demo.mybatis.service.UserService;

/**
 * Created by 264929 on 2015/6/13.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


}
