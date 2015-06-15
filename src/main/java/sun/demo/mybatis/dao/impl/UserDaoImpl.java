package sun.demo.mybatis.dao.impl;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.demo.mybatis.constants.Constants;
import sun.demo.mybatis.dao.UserDao;
import sun.demo.mybatis.entity.User;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 264929 on 2015/6/13.
 */
@Repository
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init() {
        super.setSqlSessionFactory(this.sqlSessionFactory);
    }

    @Override
    public User getUser(long id) {
        return (User) getSqlSession().selectOne(Constants.build(Constants.NAMESPACE_USER, "getUser"), id);
    }

    @Override
    public boolean addUser(String name, int age) {
        Map map = new HashMap();
        map.put("name", name);
        map.put("age", age);
        int result = getSqlSession().insert(Constants.build(Constants.NAMESPACE_USER, "addUser"), map);
        return result != -1;
    }

    @Override
    public boolean addUser2(User user) {
        int result = getSqlSession().insert(Constants.build(Constants.NAMESPACE_USER, "addUser2"), user);
        return result != -1;
    }

    @Override
    public void updateUser(User user) {
        getSqlSession().update(Constants.build(Constants.NAMESPACE_USER, "updateUser"), user);
    }

    @Override
    public User getUser(String name) {
        return (User) getSqlSession().selectOne(Constants.build(Constants.NAMESPACE_USER, "getUserByName"), name);
    }
}
