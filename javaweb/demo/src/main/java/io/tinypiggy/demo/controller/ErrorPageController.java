package io.tinypiggy.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

    @Value("${myConfig}")
    private String configName;

    // modelAndView TEst
    @RequestMapping("/errorPage")
    public String test(){
        System.out.println(configName);
        return "index";
    }
}
