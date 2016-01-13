package sun.jmx.example2;

import sun.jmx.example1.Hello;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by root on 2016/1/13.
 * <p/>
 * Create the Hello MBean and QueueSampler MXBean, register them in the platform
 * MBean server, then wait forever (or until the program is interrupted).
 */
public class Main {

    /*
       For simplicity, we declare "throws Exception".
       Real programs will usually want finer-grained exception handling.
     */
    public static void main(String[] args) throws Exception {

        // Get the Platform MBean Server
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        // Construct the ObjectName for the Hello MBean we will register
        ObjectName mbeanName = new ObjectName("sun.jmx.example1:type=Hello");

        // Create the Hello World MBean
        Hello mbean = new Hello();

        // Register the Hello World MBean
        mbs.registerMBean(mbean, mbeanName);


        // Construct the ObjectName for the QueueSampler MXBean we will register
        ObjectName mxBeanName = new ObjectName("sun.jmx.example2:type=QueueSampler");

        // Create the Queue Sampler MXBean
        Queue<String> queue = new ArrayBlockingQueue<>(10);
        queue.add("Request-1");
        queue.add("Request-2");
        queue.add("Request-3");
        QueueSampler mxBean = new QueueSampler(queue);

        // Register the Queue Sampler MXBean
        mbs.registerMBean(mxBean, mxBeanName);

        // Wait forever
        System.out.println("Waiting for incoming requests...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
