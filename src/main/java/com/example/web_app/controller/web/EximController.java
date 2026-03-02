package com.example.web_app.controller.web;

import com.example.web_app.model.SidebarNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EximController {

    @GetMapping("/{type}/exim")
    public String eximPage(
            @PathVariable String type,
            Authentication authentication,
            Model model
    ) throws IOException {

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        type = type.toLowerCase();

        // ======================
        // VALIDASI ROLE
        // ======================
        if (!role.equals("ADMIN")) {

            if (role.equals("FIG5") && !type.equals("fig5")) {
                return "redirect:/access-denied";
            }

            if (role.equals("FILW_ON") && !type.equals("filw-on")) {
                return "redirect:/access-denied";
            }

            if (role.equals("FILW_NB") && !type.equals("filw-nb")) {
                return "redirect:/access-denied";
            }
        }

        // ======================
        // MODEL
        // ======================
        model.addAttribute("primary", type.toUpperCase());
        model.addAttribute("secondary", "EXIM");
        model.addAttribute("role", role);

        // 🔥 FIX: sekarang dynamic sesuai type
        model.addAttribute(
                "sidebar",
                buildTree("pdfs/" + type.toUpperCase() + "/EXIM")
        );

        return "exim";
    }

    // ======================================
    // RECURSIVE SIDEBAR BUILDER
    // ======================================
    private List<SidebarNode> buildTree(String rootPath) throws IOException {

        List<SidebarNode> nodes = new ArrayList<>();

        Resource resource = new ClassPathResource("static/" + rootPath);
        if (!resource.exists()) return nodes;

        File rootDir = resource.getFile();
        File[] files = rootDir.listFiles();
        if (files == null) return nodes;

        for (File file : files) {

            if (file.isDirectory()) {

                SidebarNode folderNode =
                        new SidebarNode(file.getName(), true, null);

                folderNode.getChildren().addAll(
                        buildTree(rootPath + "/" + file.getName())
                );

                nodes.add(folderNode);
            }

            else if (file.isFile() &&
                    file.getName().toLowerCase().endsWith(".pdf")) {

                nodes.add(
                        new SidebarNode(
                                file.getName(),
                                false,
                                "/" + rootPath + "/" + file.getName()
                        )
                );
            }
        }

        return nodes;
    }
}