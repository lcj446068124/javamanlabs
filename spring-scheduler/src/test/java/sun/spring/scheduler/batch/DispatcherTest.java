package sun.spring.scheduler.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by root on 2016/3/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class DispatcherTest {

    @Autowired
    private JobDispatcher dispatcher;

    @Test
    public void testDispatcher() throws Exception {
        dispatcher.dispatcher();
        System.out.println("main thread done");
        Thread.sleep(Long.MAX_VALUE);
    }
}