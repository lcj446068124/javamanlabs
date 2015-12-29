package module.webservice.services;

import javax.jws.WebService;
import java.util.Date;

/**
 * Created by root on 2015/12/30.
 *
 * The @WebService property endpointInterface links the SIB(this class) to the SEI(TimeServer)
 * Note that the method implementations are not annotated as @WebMethod
 *
 */
@WebService(endpointInterface = "module.webservice.services.TimeServer")
public class TimeServerImpl implements TimeServer {
    @Override
    public String getTimeAsString() {
        return new Date().toString();
    }

    @Override
    public long getTimeAsElapsed() {
        return new Date().getTime();
    }
}
