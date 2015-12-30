package module.webservice.services.example2;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by root on 2015/12/30.
 * <p/>
 * This service is implemented as a single Java class rather than as a separate SEI and SIB.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class Teams {
    private TeamsUtility utils;

    public Teams() {
        utils = new TeamsUtility();
        utils.make_test_teams();
    }

    @WebMethod
    public Team getTeam(String name) {
        return utils.getTeam(name);
    }

    @WebMethod
    public List<Team> getTeams() {
        return utils.getTeams();
    }
}
