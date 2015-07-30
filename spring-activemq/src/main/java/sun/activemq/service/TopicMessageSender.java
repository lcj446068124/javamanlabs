package sun.activemq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by 264929 on 2015/7/29.
 */

@Component
public class TopicMessageSender {
    private static Logger logger = LoggerFactory.getLogger(TopicMessageSender.class);
    @Resource(name = "jmsTopicTemplate")
    private JmsTemplate jmsTemplate;

    public void send(final Object object) {
        jmsTemplate.convertAndSend(object);
    }
}
