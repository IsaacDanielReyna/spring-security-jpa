package com.isaacreyna.springsecurityjpa.controller;

import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    @Autowired // This means to get the bean called userRepository
    private UserRepository userRepository;

    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

}
