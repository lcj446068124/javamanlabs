package sun.activemq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.Message;

/**
 * Created by 264929 on 2015/7/29.
 */
@Component
public class TopicMessageReceiver {
    private static Logger logger = LoggerFactory.getLogger(TopicMessageReceiver.class);

    @Resource(name = "jmsTopicTemplate")
    private JmsTemplate jmsTemplate;

    @Resource(name = "topicDest")
    private Destination destination;

    public void receive() {
        Message message = jmsTemplate.receive(destination);
        logger.info("receive: " + message);
    }
}
