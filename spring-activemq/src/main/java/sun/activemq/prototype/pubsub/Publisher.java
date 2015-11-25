package sun.activemq.prototype.pubsub;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by root on 2015/11/24.
 */
public class Publisher {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Context.URL);
        Connection connection = connectionFactory.createConnection(Context.USERNAME, Context.PASSWORD);

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(Context.TOPIC);
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        TextMessage textMessage = session.createTextMessage();
        textMessage.setText("This is a topic message 14");

        producer.send(textMessage);

        connection.close();

    }
}
