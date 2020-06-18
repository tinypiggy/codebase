package io.tinypiggy.demo.aop;

import io.tinypiggy.tools.annotation.MyTag;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class WebLogAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping() {
    }

    //  @Pointcut("execution(* io.tinypiggy.demo.controller.*.*(..))")
    @Pointcut("execution(* io.tinypiggy.demo.controller.MainController.body(..))")
    public void methodPointcut() {
    }

    @Around("requestMapping() && methodPointcut()")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch sw = new StopWatch();
        String name = joinPoint.getSignature().getName();
        log.info("around start target = {}, args = {}", joinPoint.getArgs(), joinPoint.getTarget());
        try {
            sw.start();
            return joinPoint.proceed();
        } finally {
            sw.stop();
            log.info("around end: " + sw.getTotalTimeSeconds() + " - " + name);
        }
    }

    @Before("requestMapping() && methodPointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.info("before method {}", joinPoint.getSignature().getName());
    }
}