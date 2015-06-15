package sun.demo.mybatis.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.demo.mybatis.entity.Classes;

import static org.junit.Assert.*;

/**
 * Created by 264929 on 2015/6/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class ClassesDaoTest {

    @Autowired
    private ClassesDao classesDao;

    @Test
    public void testGetClassesById() throws Exception {
        Classes classes = classesDao.getClassesById(2);
        assertNotNull(classes);
        System.out.println(classes);
    }
}