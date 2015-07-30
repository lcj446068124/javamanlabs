package sun.activemq.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by 264929 on 2015/7/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TopicMessageSenderTest {

    @Autowired
    private TopicMessageSender topicMessageSender;

    @Test
    public void testSend() throws Exception {
        topicMessageSender.send("hello.");
    }
}