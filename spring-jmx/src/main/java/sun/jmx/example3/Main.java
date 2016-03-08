package sun.jmx.example3;

import com.sun.jmx.mbeanserver.JmxMBeanServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Created by sunyamorn on 3/9/16.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-jmx.xml");
        MBeanServer mBeanServer = applicationContext.getBean("mbeanServer", JmxMBeanServer.class);
//        MBeanServer mBeanServer = (MBeanServer)mBeanServerFactoryBean.getObject();
//        System.out.println(mBeanServer.getMBeanCount());
        System.out.println(mBeanServer.getMBeanCount());
        String[] domains = mBeanServer.getDomains();
        for (String domain : domains) {
            System.out.println(domain);
        }
       MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(new ObjectName("bean:name=helloBean"));
//        System.out.println(mBeanInfo.toString());
        ObjectName objectName = new ObjectName("bean:name=helloBean");
        for (MBeanAttributeInfo info : mBeanInfo.getAttributes()) {
            System.out.println(info);
        };
        System.out.println(mBeanServer.getAttribute(objectName, "CacheSize"));
    }
}
