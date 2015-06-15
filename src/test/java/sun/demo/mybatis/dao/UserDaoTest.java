package sun.demo.mybatis.dao;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.demo.mybatis.entity.User;

import static org.junit.Assert.*;

/**
 * Created by 264929 on 2015/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    @Ignore
    public void testGetUser() throws Exception {
        User user = userDao.getUser(1);
        assertNotNull(user);
        System.out.println(user.getName());
        System.out.println(user.getBirthday());
    }

    @Test
    @Ignore
    public void testAddUser() throws Exception{
        boolean result= userDao.addUser("ss",30);
        assertTrue(result);
    }

    @Test
    @Ignore
    public void testAddUser2() throws Exception {
        User user=new User();
        user.setName("pp");
        user.setAge(30);
        boolean result = userDao.addUser2(user);
        assertTrue(result);
    }

    @Test
    @Ignore
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setName("update");
        user.setAge(20);
        user.setId(6);
        userDao.updateUser(user);
    }

    @Test
    @Ignore
    public void testGetUserByName() throws Exception {
        User user = userDao.getUser("test");
        assertNotNull(user);
        System.out.println(user.getName());
    }
}