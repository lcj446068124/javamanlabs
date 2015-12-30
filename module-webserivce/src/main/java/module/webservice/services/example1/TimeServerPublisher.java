package module.webservice.services.example1;

import javax.xml.ws.Endpoint;

/**
 * Created by root on 2015/12/30.
 *
 *
 * This application publishes the web service whose SIB is TimeServerImpl. For now, the service is
 * published at network address 127.0.0.1 which is localhost, and at port number 8080, as this port
 * is likely available on any desktop machine. The publication path is /ts, an arbitrary name.
 *
 * The application runs indefinitely, awaiting service requests.
 * It needs to be terminated at the command prompt which control-C or the equivalent.
 *
 * Once the application is started, open a browser to the URL
 *      http://127.0.0.1:8080/ts?wsdl
 * to view the service contract, the WSDL document. This is an easy test to determine whether the
 * service has deployed successfully. If test succeeds, a client then can be executed against the
 * service.
 *
 */
public class TimeServerPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:8080/ts", new TimeServerImpl());
    }
}
