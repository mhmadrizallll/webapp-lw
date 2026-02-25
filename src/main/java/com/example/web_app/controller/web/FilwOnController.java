package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilwOnController {

  @GetMapping("/filw-on")
  public String filwOnPage() {
    return "filw-on"; // mengarah ke templates/filw-on.html
  }
}
