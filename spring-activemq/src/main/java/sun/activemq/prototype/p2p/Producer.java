package sun.activemq.prototype.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by root on 2015/11/24.
 */
public class Producer {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Context.URL);
        Connection connection = connectionFactory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(Context.SUBJECT);

        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        connection.start();

        // send a message
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText("hello world 2");

        // send it
        producer.send(textMessage);

        connection.close();
    }

}
