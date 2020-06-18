package io.tinypiggy.demo;

import io.tinypiggy.tools.annotation.MyTag;
import io.tinypiggy.tools.service.MyAction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MyTag
public class DemoApplication {

    public static void main(String[] args) {
        // 通过 spi 注入实现
        new MyAction().action();
        SpringApplication.run(DemoApplication.class, args);
    }

}
