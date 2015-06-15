package sun.demo.mybatis.dao;

import sun.demo.mybatis.entity.User;

/**
 * Created by 264929 on 2015/6/13.
 */
public interface UserDao {

    /*
        Simple preparedStatement parameter
     */
    public User getUser(long id);

    /*
        Use hashMap as parameterType
     */
    public boolean addUser(String name, int age);

    /*
        User java bean as parameterType
     */
    public boolean addUser2(User user);

    public void updateUser(User user);

    public User getUser(String name);



}
