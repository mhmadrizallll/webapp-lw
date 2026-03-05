package com.example.web_app.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String loginPage(
    HttpServletRequest request,
    Authentication authentication,
    Model model
  ) {
    // 🔒 Kalau sudah login → redirect sesuai role
    if (
      authentication != null &&
      authentication.isAuthenticated() &&
      !authentication.getPrincipal().equals("anonymousUser")
    ) {
      String role = authentication
        .getAuthorities()
        .iterator()
        .next()
        .getAuthority();

      return switch (role) {
        case "ROLE_ADMIN" -> "redirect:/";
        case "ROLE_FIG5" -> "redirect:/fig5";
        case "ROLE_FILW_NB" -> "redirect:/filw-nb";
        case "ROLE_FILW_ON" -> "redirect:/filw-on";
        default -> "redirect:/";
      };
    }

    // 🔥 Alert akses ditolak
    String denied = (String) request.getSession().getAttribute("DENIED_ROLE");

    if (denied != null) {
      switch (denied) {
        case "FILW_NB" -> model.addAttribute(
          "customError",
          "Silahkan login dengan akun filw-nb"
        );
        case "FILW_ON" -> model.addAttribute(
          "customError",
          "Silahkan login dengan akun filw-on"
        );
        case "FIG5" -> model.addAttribute(
          "customError",
          "Silahkan login dengan akun fig5"
        );
      }

      request.getSession().removeAttribute("DENIED_ROLE");
    }

    return "login";
  }

  // 🔥 LOGIN SUCCESS PAGE
  @GetMapping("/login-success")
  public String loginSuccess(HttpServletRequest request, Model model) {
    String role = (String) request.getSession().getAttribute("LOGIN_ROLE");

    if (role == null) {
      return "redirect:/login";
    }

    String redirectUrl = switch (role) {
      case "ROLE_ADMIN" -> "/";
      case "ROLE_FIG5" -> "/fig5";
      case "ROLE_FILW_NB" -> "/filw-nb";
      case "ROLE_FILW_ON" -> "/filw-on";
      default -> "/";
    };

    model.addAttribute("redirectUrl", redirectUrl);

    request.getSession().removeAttribute("LOGIN_ROLE");

    return "login-success";
  }
}
