package sun.spring.scheduler.support.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView index(ModelAndView modelAndView){
        modelAndView.addObject("list", dataAccessOperation.queryAll());
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
