package com.example.web_app.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout-success")
    public String logoutSuccess(HttpServletRequest request, Model model) {
        // URL tujuan redirect manual
        String redirectUrl = "/login";

        // Masukkan ke model untuk Thymeleaf
        model.addAttribute("redirectUrl", redirectUrl);

        return "logout-success"; // nama file Thymeleaf: logout-success.html
    }
}