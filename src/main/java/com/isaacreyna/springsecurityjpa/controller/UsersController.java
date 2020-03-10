package com.isaacreyna.springsecurityjpa.controller;

import com.isaacreyna.springsecurityjpa.model.User;
import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping(path="/users")
public class UsersController {
    // TODO: Create user
    // TODO: Retrieve user
    // TODO: Update user
    // TODO: Delete user(s)

    @Autowired // This means to get the bean called userRepository
    private UserRepository userRepository;

    @GetMapping("")
    public String retrieveUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/create")
    public String createUser(){
        return "users";
    }

    @GetMapping("/retrieve")
    public String retrieveUser(){
        return "users";
    }

    @GetMapping("/edit/{uid}")
    public String editUser(@PathVariable("uid") int userID, Model model){
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            model.addAttribute("user", user);
        } else {
            // TODO: redirect to 404?
            System.out.println("user not found");
        }

        System.out.println(userID);
        return "users/edit";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("uid") int userID){

        //TODO: Only admins roles can modify database records.
        //TODO: Delete or reassign data created from user.
        //TODO: Show delete success/error indicator.
        //TODO: Check if user with uid exists before trying to delete.
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()){
            userRepository.deleteById(userID);
        } else {
            System.out.println("User does not exist");
        }

        return "redirect:/users";
    }

}
