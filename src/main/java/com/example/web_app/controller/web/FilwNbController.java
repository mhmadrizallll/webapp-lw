package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilwNbController {

  @GetMapping("/filw-nb")
  public String filwNbPage(Model model) {
    model.addAttribute("primary", "FILW-NB");

    return "filw-nb";
  }
}
