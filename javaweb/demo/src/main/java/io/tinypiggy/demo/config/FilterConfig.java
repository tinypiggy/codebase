package io.tinypiggy.demo.config;

import io.tinypiggy.demo.filter.MyFilter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean getFilterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter());
        //filterRegistrationBean.addUrlPatterns(new String[]{"*.do","*.jsp"});
        filterRegistrationBean.addUrlPatterns("/main");
        return  filterRegistrationBean;
    }
}
