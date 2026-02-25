package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Fig5Controller {

  @GetMapping("/fig5")
  public String fig5Page() {
    return "fig5"; // mengarah ke templates/filw-nb.html
  }
}
