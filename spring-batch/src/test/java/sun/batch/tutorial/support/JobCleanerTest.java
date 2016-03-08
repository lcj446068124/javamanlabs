package sun.batch.tutorial.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by root on 2016/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class JobCleanerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    public void testCleanBeforeJobLaunch() throws Exception {
        JobCleaner jobCleaner = new JobCleaner(jdbcTemplate);
        jobCleaner.cleanBeforeJobLaunch("transferDataToRedisJob");
    }
}