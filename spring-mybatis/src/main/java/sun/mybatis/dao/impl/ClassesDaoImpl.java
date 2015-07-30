package sun.mybatis.dao.impl;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.mybatis.constants.Constants;
import sun.mybatis.dao.ClassesDao;
import sun.mybatis.entity.Classes;

import javax.annotation.PostConstruct;

/**
 * Created by 264929 on 2015/6/15.
 */
@Repository
public class ClassesDaoImpl extends SqlSessionDaoSupport implements ClassesDao {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init() {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public Classes getClassesById(int id) {
        return (Classes) getSqlSession().selectOne(Constants.build(Constants.NAMESPACE_CLASSES, "getClasses2"), id);
    }
}
