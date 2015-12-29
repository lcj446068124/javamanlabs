package module.webservice.services;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by root on 2015/12/30.
 */
public class TimeClient {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://127.0.0.1:8080/ts?wsdl");

        // Qualified name of the service:
        // 1st arg is the service URI
        // 2nd is the service name published in the WSDL
        QName qName = new QName("http://services.webservice.module/", "TimeServerImplService");

        // Create, in effect , a factory for service
        Service service = Service.create(url, qName);

        // Extract the endpoint interface, the service "port"
        TimeServer timeServer = service.getPort(TimeServer.class);
        System.out.println(timeServer.getTimeAsString());
        System.out.println(timeServer.getTimeAsElapsed());

    }
}
