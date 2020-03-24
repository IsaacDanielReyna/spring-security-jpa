package com.isaacreyna.springsecurityjpa.controller;

import com.isaacreyna.springsecurityjpa.model.Role;
import com.isaacreyna.springsecurityjpa.model.User;
import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired // Gets bean called userRepository
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

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

            // TODO: Role Ids change, so implement findByRoleName("ROLE_USER"), and pass the object to the user
            // create a single role
            Role role = new Role();
            role.setId(2);
            role.setName("ROLE_USER");

            // create a list of roles
            List<Role> roles = new LinkedList<Role>();
            roles.add(role);

            // add roles to the user
            user.setRoles(roles);
            user.setActive(true);

            try{
                userRepository.save(user);
            } catch (Exception e) {
                // TODO: Display database error
                return "register";
            }

            return "redirect:/login";
        }

        // Display error: username and email already registered
        FieldError _userNameError = new FieldError("user", "userName", "Username already in use");
        FieldError _emailError = new FieldError("user", "email", "Email already in use");
        result.addError(_userNameError);
        result.addError(_emailError);
        return "register";
    }
}
