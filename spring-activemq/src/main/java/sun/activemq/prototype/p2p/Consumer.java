package sun.activemq.prototype.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by root on 2015/11/24.
 */
public class Consumer {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Context.URL);
        Connection connection = connectionFactory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(Context.SUBJECT);

        MessageConsumer consumer = session.createConsumer(destination);

        connection.start();

        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Received message"
                    + textMessage.getText() + "'");
        }




    }
}
