package com.isaacreyna.springsecurityjpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {
    @GetMapping("/user")
    public String user(){
        return ("<h1>Welcome user</h1>");
    }

    @GetMapping("/admin-orig")
    public String admin(){
        return ("<h1>Welcome admin</h1>");
    }
}
