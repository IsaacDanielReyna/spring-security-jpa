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

    @Autowired // This means to get the bean called userRepository
    private UserRepository userRepository;

    @GetMapping("")
    public String retrieveUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }



    // TODO: Replace hardcoded roles from database.
    @GetMapping("/add")
    public String showAddUser(User user){
        return "users/add";
    }

    @PostMapping("/add")
    public String addUser(@Valid User user, BindingResult result) {

        if (result.hasErrors()) {
            return "users/add";
        }
        // Check if username or email already exists
        Optional<User> u = userRepository.findByUserNameOrEmail(user.getUserName(), user.getEmail());
        if (!u.isPresent()) {

            user.setActive(true);

            try {
                userRepository.save(user);
            } catch (Exception e) {
                // TODO: Display database error
                return "users/add";
            }
        }

        return "redirect:/users/";
    }
    @GetMapping("/view/{uid}")
    public String viewUser(@PathVariable("uid") int userID, Model model){
        Optional<User> user = userRepository.findById(userID);
        if (!user.isPresent()) {
            return "redirect:/users/";
        }

        model.addAttribute("user", user.get());
        return "profile";
    }

    @GetMapping("/edit/{uid}")
    public String editUser(@PathVariable("uid") int userID, Model model){
        Optional<User> user = userRepository.findById(userID);
        if (!user.isPresent()) {
            return "redirect:/users/";
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
