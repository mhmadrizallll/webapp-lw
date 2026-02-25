package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FilwNbController {

  @GetMapping("/filw-nb")
  public String filwNbPage() {
    return "filw-nb"; // mengarah ke templates/filw-nb.html
  }
}
