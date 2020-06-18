package io.tinypiggy.demo.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {NullPointerException.class, FileNotFoundException.class})
    public String errorHandler(Exception e){
        return e.getMessage();
    }
}
