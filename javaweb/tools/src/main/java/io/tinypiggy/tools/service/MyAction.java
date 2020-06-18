package io.tinypiggy.tools.service;

import java.util.ServiceLoader;

public class MyAction {

    public void action(){
        ServiceLoader<DefaultService> services = ServiceLoader.load(DefaultService.class);
        for(DefaultService service : services){
            service.func();
        }
    }
}
