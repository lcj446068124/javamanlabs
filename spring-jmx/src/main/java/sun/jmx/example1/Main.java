package sun.jmx.example1;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Created by root on 2016/1/13.
 *
 * To monitor the Main JMX agent remotely, you should config the VM options:
 *  -Dcom.sun.management.jmxremote.port=9999
 *  -Dcom.sun.management.jmxremote.authenticate=false
 *  -Dcom.sun.management.jmxremote.ssl=false
 */
public class Main {
    public static void main(String[] args) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("sun.jmx.example:type=Hello");
        Hello mbean = new Hello();
        mbs.registerMBean(mbean, name);

        System.out.println("Waiting forever...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
