package com.isaacreyna.springsecurityjpa.controller;

import com.isaacreyna.springsecurityjpa.model.User;
import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {
    @Autowired // Gets bean called userRepository
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // TODO: Fix logout redirection
    @GetMapping("/logout")
    public String logout(){
        return "logout";
    }

    @GetMapping("/register")
    public String register(User user){

        return "register";
    }

    @PostMapping("/register")
    public String checkRegistration(@Valid User user, BindingResult result){

        if (result.hasErrors()){
            return "register";
        }

        // Check if username or email already exists
        Optional<User> u = userRepository.findByUserNameOrEmail(user.getUserName(), user.getEmail());
        if (!u.isPresent()){

            user.setRoles("ROLE_USER");
            user.setActive(true);

            try{
                userRepository.save(user);
            } catch (Exception e) {
                // TODO: Display database error
                return "register";
            }

            return "redirect:/profile";
        }

        // Display error: username and email already registered
        FieldError _userNameError = new FieldError("user", "userName", "Username already in use");
        FieldError _emailError = new FieldError("user", "email", "Email already in use");
        result.addError(_userNameError);
        result.addError(_emailError);
        return "register";
    }
}
