package sun.activemq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by 264929 on 2015/7/30.
 */
public class QueueMessageListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    @Override
    public void onMessage(Message message) {
        logger.info("listen: " + message);
    }
}
