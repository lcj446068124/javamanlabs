package sun.activemq.prototype.pubsub;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by root on 2015/11/24.
 */
public class Subscriber {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Context.URL);
        Connection connection = connectionFactory.createConnection(Context.USERNAME, Context.PASSWORD);
        connection.setClientID("sub-client1");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(Context.TOPIC);

        MessageConsumer consumer = session.createDurableSubscriber(topic, "subname");
        Message message = consumer.receive();

        TextMessage textMessage = (TextMessage)message;
        System.out.println("Receive Message: " + textMessage.getText());

        connection.close();
    }
}
