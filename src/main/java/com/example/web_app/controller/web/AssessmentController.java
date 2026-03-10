package com.example.web_app.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.web_app.service.FileTreeService;

@Controller
public class AssessmentController {

    private final FileTreeService fileTreeService;

    public AssessmentController(FileTreeService fileTreeService) {
        this.fileTreeService = fileTreeService;
    }

    @GetMapping("/{type}/assessment")
    public String assessmentPage(
            @PathVariable String type,
            Authentication authentication,
            Model model
    ) {

        String role = authentication
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        type = type.toUpperCase();

        if (!role.equals("ADMIN")) {

            if (role.equals("FIG5") && !type.equals("FIG5"))
                return "redirect:/access-denied";

            if (role.equals("FILW_ON") && !type.equals("FILW_ON"))
                return "redirect:/access-denied";

            if (role.equals("FILW_NB") && !type.equals("FILW_NB"))
                return "redirect:/access-denied";
        }

        model.addAttribute("primary", type);
        model.addAttribute("secondary", "ASSESSMENT");
        model.addAttribute("role", role);

        model.addAttribute("sidebar", fileTreeService.buildTree(type + "/ASSESSMENT"));

        return "assessment";
    }
}