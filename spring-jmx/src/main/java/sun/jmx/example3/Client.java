package sun.jmx.example3;

import sun.jmx.example2.QueueSample;
import sun.jmx.example2.QueueSamplerMXBean;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by sunyamorn on 1/13/16.
 */
public class Client {

    /**
     * Inner class that will handle the notifications.
     */
    public static class ClientListener implements NotificationListener {
        @Override
        public void handleNotification(Notification notification, Object handback) {
            echo("\nReceived notification:");
            echo("\tClassName: " + notification.getClass().getName());
            echo("\tSource: " + notification.getSource());
            echo("\tType: " + notification.getType());
            echo("\tMessage: " + notification.getMessage());
            if (notification instanceof AttributeChangeNotification) {
                AttributeChangeNotification acn =
                        (AttributeChangeNotification) notification;
                echo("\tAttributeName: " + acn.getAttributeName());
                echo("\tAttributeType: " + acn.getAttributeType());
                echo("\tNewValue: " + acn.getNewValue());
                echo("\tOldValue: " + acn.getOldValue());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        /*
            Create and RMI connector client and connect it to the RMI connector server.
         */
        echo("\nCreate an RMI connector client and connect it to the RMI connector server.");
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        // Create Listener
        ClientListener listener = new ClientListener();

        // Get and MBeanServerConnection
        echo("\nGet and MBeanServerConnection");
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        waitForEnterPressed();

        // Get domains from MBeanServer
        echo("\nDomains:");
        String domains[] = mbsc.getDomains();
        Arrays.sort(domains);
        for (String domain : domains) {
            echo("\tDomain = " + domain);
        }
        waitForEnterPressed();

        // Get MBeanServer default domain
        echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

        // Get MBean count
        echo("\nMbean count = " + mbsc.getMBeanCount());

        // Query MBean names
        echo("\nQuery MBeanServer MBeans:");
        Set<ObjectName> names = new TreeSet<>(mbsc.queryNames(null, null));
        for (ObjectName name : names) {
            echo("\tObjectName = " + name);
        }
        waitForEnterPressed();


        // ----------------------
        // Manage the Hello MBean
        // ----------------------

        echo("\n>>> Perform operations on Hello MBean <<<");

        // Construct the ObjectName for the QueueSampler MXBean
        ObjectName mxBeanName = new ObjectName("sun.jmx.example2:type=QueueSampler");

        // Create a dedicated proxy for the MXBean instead of going directly through the MBean server connection
        QueueSamplerMXBean mxBeanProxy = JMX.newMXBeanProxy(mbsc, mxBeanName, QueueSamplerMXBean.class);

        // Get QueueSample attribute in QueueSampler MXBean
        QueueSample queue1 = mxBeanProxy.getQueueSample();
        echo("\nQueueSample.Date = " + queue1.getDate());
        echo("QueueSample.Head = " + queue1.getHead());
        echo("QueueSample.Size = " + queue1.getSize());


        // Invoke "clearQueue" in QueueSampler MXBean
        echo("\nInvoke clearQueue() in QueueSampler MXBean...");
        mxBeanProxy.clearQueue();

        // Get QueueSample attribute in QueueSampler MXBean
        QueueSample queue2 = mxBeanProxy.getQueueSample();
        echo("\nQueueSample.Date = " + queue2.getDate());
        echo("QueueSample.Head = " + queue2.getHead());
        echo("QueueSample.Size = " + queue2.getSize());

        waitForEnterPressed();

        // Close MBeanServer connection
        echo("\nClose the connection to the server");
        jmxc.close();
        echo("\nBye! Bye!");
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }

    private static void waitForEnterPressed() {
        try {
            echo("\nPress <Enter> to continue...");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
