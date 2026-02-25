package com.example.web_app.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApiController {

  @GetMapping("/api/test")
  public String test() {
    return "API JALAN Yahhh kenapa html nya gakk, ada yang tauuu";
  }
}
