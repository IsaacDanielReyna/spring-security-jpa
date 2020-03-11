package com.isaacreyna.springsecurityjpa.controller;

import com.isaacreyna.springsecurityjpa.model.User;
import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
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
        if (!user.isPresent()) {
            return "redirect:/users";
        }

        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid User user, BindingResult result){
        //TODO: Add custom password validation.
        if (result.hasErrors()){
            return "users/edit";
        }

        // Identify of entities is defined by their primary keys (user.id)
        userRepository.save(user);

        //TODO: Add update successful indicator
        return "redirect:/users/edit/" + user.getId();
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("uid") int userID){

        //TODO: Only admins roles can modify database records.
        //TODO: Delete or reassign data created from user.
        //TODO: Show delete success/error indicator.
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()){
            userRepository.deleteById(userID);
        } else {
            System.out.println("User does not exist");
        }

        return "redirect:/users";
    }

}
