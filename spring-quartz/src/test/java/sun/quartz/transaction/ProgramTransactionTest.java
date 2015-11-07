package sun.quartz.transaction;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by root on 2015/11/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config.xml"})
public class ProgramTransactionTest {
    @Autowired
    private ProgramTransaction programTransaction;


    @Test
    public void testUpdateCustomer() throws Exception {
        int result = programTransaction.updateCustomer();
        assertTrue(result > 0);
    }

    @Ignore
    @Test
    public void testDoTransactionInThreadPool(){
        programTransaction.doTransactionInThreadPool();
    }
}