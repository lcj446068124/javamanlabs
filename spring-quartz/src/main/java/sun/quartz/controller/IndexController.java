package sun.quartz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by root on 2015/11/6.
 */

@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping("/index")
    public String welcome(){
        return "index";
    }
}
