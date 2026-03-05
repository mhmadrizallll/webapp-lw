package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Fig5Controller {

  @GetMapping("/fig5")
  public String fig5Page(Model model) {
    model.addAttribute("primary", "FIG5");

    return "fig5";
  }
}
