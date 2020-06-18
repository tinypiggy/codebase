package io.tinypiggy.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@RestController
public class MainController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/main")
    public String body(HttpServletRequest request){
        System.out.println(request.getMethod());
        return "main";
    }

    @RequestMapping("/file")
    public String error() throws FileNotFoundException {
        throw new FileNotFoundException("hello");
    }
}
