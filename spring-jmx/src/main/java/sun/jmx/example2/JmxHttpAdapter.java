package sun.jmx.example2;

//import com.sun.jdmk.comm.HtmlAdaptorServer;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by sunyamorn on 1/13/16.
 *
 *
 *  Dependencies:
 *      jmxtools-1.2.1
 *      jmxri-1.2.1
 *
 *  Download URL: http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html
 *
 *  Import Maven Repository:
 *
 *      mvn install:install-file -DgroupId=com.sun.jdmk -DartifactId=jmxtools -Dversion=1.2.1 -Dpackaging=jar -Dfile=jmxtools.jar
 *
 *      mvn install:install-file -DgroupId=com.sun.jmx -DartifactId=jmxri -Dversion=1.2.1 -Dpackaging=jar -Dfile=jmxri.jar
 *
 *  Add Dependencies to pom.xml
 *
 *               <dependency>
 *                   <groupId>com.sun.jdmk</groupId>
 *                   <artifactId>jmxtools</artifactId>
 *                   <version>1.2.1</version>
 *                </dependency>
 *                <dependency>
 *                   <groupId>com.sun.jmx</groupId>
 *                   <artifactId>jmxri</artifactId>
 *                   <version>1.2.1</version>
 *                </dependency>
 *
 */
public class JmxHttpAdapter {
    public static void main(String[] args) throws Exception{
//        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//
//        HtmlAdaptorServer htmlAdaptorServer = new HtmlAdaptorServer(8888);
//        ObjectName objectName = new ObjectName("Adaptor:name=html,port=8888");
//        mbs.registerMBean(htmlAdaptorServer, objectName);
//        htmlAdaptorServer.start();
//        Thread.sleep(Long.MAX_VALUE);
    }
}
