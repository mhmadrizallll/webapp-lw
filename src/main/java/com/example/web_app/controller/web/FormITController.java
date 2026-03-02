package com.example.web_app.controller.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FormITController {

    @GetMapping("/form_it")
    public String formITPage(Model model) throws IOException {

        model.addAttribute("primary", "FORM_IT");

        // 🔥 AUTO READ PDF
        model.addAttribute("files", getPdfFiles("pdfs/FORM-IT"));

        return "form_it";
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