package module.webservice.services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by root on 2015/12/30.
 *
 * The annotation @WebService signals that this is the SEI (Service Endpoint Interface).
 * @WebMethod signals that each method is a service operation
 *
 * The @SOAPBinding annotation impacts the under-the-hood construction of the service contract,
 * the WSDL (Web Service Definition Language) document. Style.RPC simplifies the contract and
 * makes deployment easier.
 */

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface TimeServer {

    @WebMethod
    String getTimeAsString();

    @WebMethod
    long getTimeAsElapsed();
}
