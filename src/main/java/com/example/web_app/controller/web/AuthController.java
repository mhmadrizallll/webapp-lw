package com.example.web_app.controller.web;

import com.example.web_app.model.User;
import com.example.web_app.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET form register
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // templates/register.html
    }

    // POST submit form
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if(userRepository.findByUsername(user.getUsername()) != null){
            model.addAttribute("error", "Username already exists!");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        model.addAttribute("success", "User registered successfully!");
        return "register";
    }
}