package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Authentication authentication) {

        // Jika sudah login (bukan anonymous)
        if (authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken)) {

            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/";
            }

            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_FIG5"))) {
                return "redirect:/fig5";
            }

            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_FILW_NB"))) {
                return "redirect:/filw-nb";
            }

            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_FILW_ON"))) {
                return "redirect:/filw-on";
            }

            return "redirect:/";
        }

        return "login";
    }
}