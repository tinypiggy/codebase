package io.tinypiggy.demo.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private IndexService indexService;
    // mandatory adj.	强制的; 法定的; 义务的; n.	受托者;


    public IndexService getIndexService() {
        return indexService;
    }

    public UserService() {
        System.out.println("constructor from userService");
    }
}
