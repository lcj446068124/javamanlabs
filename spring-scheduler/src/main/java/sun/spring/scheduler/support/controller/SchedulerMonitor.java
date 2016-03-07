package sun.spring.scheduler.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by root on 2016/3/7.
 */
@Controller
@RequestMapping("/")
public class SchedulerMonitor {
    @RequestMapping("/scheduler")
    public String index(){
        return "index";
    }
}
