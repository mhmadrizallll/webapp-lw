package com.example.web_app.controller.web;

import com.example.web_app.model.SidebarNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Fig5ItController {

    // Folder sejajar dengan project
    private final String BASE_DIR = "../";

    // ==========================================
    // HRD PAGE
    // ==========================================
    @GetMapping("/{type}/it")
    public String itPage(
            @PathVariable String type,
            Authentication authentication,
            Model model
    ) {

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

        model.addAttribute("primary", type.toUpperCase());
        model.addAttribute("secondary", "IT");
        model.addAttribute("role", role);

        model.addAttribute(
                "sidebar",
                buildTree(type + "/IT")
        );

        return "it";
    }

    // ==========================================
    // BUILD SIDEBAR TREE (RECURSIVE)
    // ==========================================
    private List<SidebarNode> buildTree(String rootPath) {

        List<SidebarNode> nodes = new ArrayList<>();

        File rootDir = new File(BASE_DIR + rootPath);

        if (!rootDir.exists() || !rootDir.isDirectory()) {
            return nodes;
        }

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

            else if (file.isFile() && isSupportedFile(file.getName())) {

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

    // ==========================================
    // SUPPORTED FILE TYPES
    // ==========================================
    private boolean isSupportedFile(String fileName) {

        String lower = fileName.toLowerCase();

        return lower.endsWith(".pdf")
                || lower.endsWith(".doc")
                || lower.endsWith(".docx")
                || lower.endsWith(".xls")
                || lower.endsWith(".xlsx")
                || lower.endsWith(".ppt")
                || lower.endsWith(".pptx");
    }
}