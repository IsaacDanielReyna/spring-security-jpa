package com.isaacreyna.springsecurityjpa.controller;

import com.isaacreyna.springsecurityjpa.model.User;
import com.isaacreyna.springsecurityjpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
        return "users/index";
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
        return "users/view";
    }

    @GetMapping("/edit")
    public String edit(){
        return "redirect:/users/";
    }

    @GetMapping("/edit/{uid}")
    public String editUser(@PathVariable("uid") int userID, Model model){
        Optional<User> user = userRepository.findById(userID);
        if (!user.isPresent()) {
            return "redirect:/users/";
        }
        model.addAttribute("uid", userID);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/edit/{uid}")
    public String editUser(@Valid User user, BindingResult result, @PathVariable("uid") int userID, Model model, RedirectAttributes redirectAttributes){
        //TODO: Add custom password validation.
        if (result.hasErrors()){
            model.addAttribute("uid", userID);
            model.addAttribute("message", "Check all fields and try again.");
            model.addAttribute("alertClass", "alert-danger");
            return "users/edit";
        }

        // Entities are defined by their primary keys (user.id)
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "User updated");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/users/edit/" + user.getId();
    }

    @PostMapping("/edit")
    public String updateUser(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes){
        //TODO: Add custom password validation.
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("message", "Check all fields and try again.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "users/edit";
        }

        // Entities are defined by their primary keys (user.id)
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "User updated");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/users/edit/" + user.getId();
    }

    @GetMapping("/delete/{uid}")
    public String deleteUser(@PathVariable("uid") int userID){

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
