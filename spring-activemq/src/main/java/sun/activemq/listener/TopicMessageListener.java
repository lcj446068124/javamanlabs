package sun.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by 264929 on 2015/7/30.
 */
public class TopicMessageListener implements MessageListener{
    private static Logger logger = LoggerFactory.getLogger(TopicMessageListener.class);
    @Override
    public void onMessage(Message message) {
        logger.info("Listen: " + message);
    }
}
