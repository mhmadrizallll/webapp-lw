package com.example.web_app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InformasiITController {

  @GetMapping("/informasi_it")
  public String informasiItPage(Model model) {

      model.addAttribute("primary", "INFORMASI_IT");

      return "informasi_it";
  }
}
