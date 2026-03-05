package com.example.web_app.controller.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProsedurITController {

  @GetMapping("/prosedur_it")
  public String prosedurITPage(Model model) throws IOException {
    model.addAttribute("primary", "PROSEDUR_IT");

    // 🔥 AUTO READ PDF
    model.addAttribute("files", getPdfFiles("pdfs/PROSEDUR-IT"));

    return "prosedur_it";
  }

  private List<String> getPdfFiles(String path) throws IOException {
    List<String> fileList = new ArrayList<>();

    Resource resource = new ClassPathResource("static/" + path);
    if (!resource.exists()) return fileList;

    File folder = resource.getFile();
    File[] files = folder.listFiles();
    if (files == null) return fileList;

    for (File file : files) {
      if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
        fileList.add(file.getName());
      }
    }

    return fileList;
  }
}
