package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilwOnController {

  @GetMapping("/filw-on")
  public String filwOnPage(Model model) {

      model.addAttribute("primary", "FILW-ON");

      return "filw-on";
  }
}
