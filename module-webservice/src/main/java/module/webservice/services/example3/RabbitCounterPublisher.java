package module.webservice.services.example3;

import javax.xml.ws.Endpoint;

/**
 * Created by root on 2015/12/30.
 */
public class RabbitCounterPublisher {
    public static void main(String[] args) {
        int port = 8888;
        String url = "http://localhost:" + port + "/teams";
        System.out.println("Publishing Teams on port " + port);
        Endpoint.publish(url, new RabbitCounter());
    }
}
