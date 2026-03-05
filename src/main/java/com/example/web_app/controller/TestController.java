package com.example.web_app.controller;

import com.example.web_app.model.Test;
import com.example.web_app.repository.TestRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  private final TestRepository testRepository;

  public TestController(TestRepository testRepository) {
    this.testRepository = testRepository;
  }

  @GetMapping("/test")
  public List<Test> getAllTest() {
    return testRepository.findAll();
  }
}
