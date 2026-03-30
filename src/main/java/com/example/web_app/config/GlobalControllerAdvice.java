package com.example.web_app.config;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ModelAttribute
  public void addUserInfo(Model model, Authentication authentication) {
    if (
      authentication != null &&
      authentication.isAuthenticated() &&
      !authentication.getPrincipal().equals("anonymousUser")
    ) {
      String username = authentication.getName();

      String cleanRole = authentication
        .getAuthorities()
        .stream()
        .map(auth -> auth.getAuthority())
        .filter(auth -> auth.startsWith("ROLE_"))
        .map(auth -> auth.replace("ROLE_", ""))
        .findFirst()
        .orElse("USER");

      model.addAttribute("loggedInUsername", username);
      model.addAttribute("loggedInRole", cleanRole);
    }
  }
}