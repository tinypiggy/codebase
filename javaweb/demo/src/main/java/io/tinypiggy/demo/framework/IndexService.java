package io.tinypiggy.demo.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexService {

    @Autowired
    private UserService userService;

    public IndexService() {
        System.out.println("constructor from indexService");
    }

    public UserService getUserService() {
        return userService;
    }
}
