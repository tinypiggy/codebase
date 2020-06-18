package io.tinypiggy.demo.spi;

import io.tinypiggy.tools.service.DefaultService;

public class MyService implements DefaultService {
    @Override
    public void func() {
        System.out.println("spi implements with MyService");
    }
}
