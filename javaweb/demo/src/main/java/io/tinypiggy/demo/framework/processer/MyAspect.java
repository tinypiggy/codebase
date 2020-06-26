package io.tinypiggy.demo.framework.processer;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspect {

    @Pointcut("execution(* io.tinypiggy.demo.framework.UserService.getIndexService(..))")
    public void somsMethods(){

    }

    @Before("somsMethods()")
    public void before(){
        System.out.println("------------before--------------");
    }
}

