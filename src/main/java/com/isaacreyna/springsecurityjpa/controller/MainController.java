package com.isaacreyna.springsecurityjpa.controller;

import com.isaacreyna.springsecurityjpa.model.User;
import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
    @Autowired // This means to get the bean called userRepository
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public String addNewUser (@RequestParam String userName, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password
    ) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User u = new User();
        u.setUserName(userName);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPassword(password);
        u.setEmail(email);
        u.setRoles("ROLE_USER");
        u.setActive(true);
        userRepository.save(u);

        return "redirect:/demo/all";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }


}