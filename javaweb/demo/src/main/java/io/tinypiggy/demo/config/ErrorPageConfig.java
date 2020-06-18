package io.tinypiggy.demo.config;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class ErrorPageConfig {

    @Bean
    public ErrorViewResolver errorViewResolver(){
        return (request, status, model) -> new ModelAndView("errorPage");
    }

}
