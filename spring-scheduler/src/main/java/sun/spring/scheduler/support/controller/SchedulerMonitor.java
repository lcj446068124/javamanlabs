package sun.spring.scheduler.support.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.spring.scheduler.core.EntranceGuard;
import sun.spring.scheduler.dao.DataAccessOperation;
import sun.spring.scheduler.domain.JobEntity;

/**
 * Created by root on 2016/3/7.
 */
@Controller
@RequestMapping("/")
public class SchedulerMonitor {

    @Autowired
    private DataAccessOperation<JobEntity, String> dataAccessOperation;

    @RequestMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.addObject("list", dataAccessOperation.queryAll());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "release", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    String releaseLock(@RequestParam String id) {
        boolean result = dataAccessOperation.releaseJobLock(id);
        JsonObject json = new JsonObject();
        json.addProperty("status", result);
        return json.toString();
    }


    @RequestMapping(value = "hangup", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public
    @ResponseBody
    String hangup(@RequestParam String id, @RequestParam int flag) {
        boolean result = dataAccessOperation.hangup(id, flag);
        JsonObject json = new JsonObject();
        json.addProperty("status", result);
        return json.toString();
    }
}
