package com.example.web_app.controller.web;

import com.example.web_app.model.PdfItem;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Controller
public class AmController {

    @GetMapping("/{type}/am")
    public String amPage(
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
model.addAttribute("secondary", "AM");

// TRUE hanya kalau FIG5
// model.addAttribute("isFig5", type.equals("fig5"));

// Role untuk navbar dinamis
model.addAttribute("role", role);



// Sidebar selalu ambil data dari FIG5/AM
model.addAttribute(
        "sidebar",
        readSidebarTwoLevel("pdfs/FIG5/AM")
);

        return "am";
    }

    // ======================================
    // SIDEBAR
    // ======================================
    private Map<String, Map<String, List<PdfItem>>> readSidebarTwoLevel(String rootPath)
            throws IOException {

        Map<String, Map<String, List<PdfItem>>> result = new LinkedHashMap<>();

        String[] mainFolders = {
                "CHART ORG",
                "FORM",
                "JOB DESC",
                "PROCEDURE",
                "STANDARD"
        };

        for (String main : mainFolders) {

            Map<String, List<PdfItem>> subMap = new LinkedHashMap<>();

            Resource mainRes =
                    new ClassPathResource("static/" + rootPath + "/" + main);

            if (!mainRes.exists()) {
                result.put(main, subMap);
                continue;
            }

            File mainDir = mainRes.getFile();
            File[] items = mainDir.listFiles();
            if (items == null) {
                result.put(main, subMap);
                continue;
            }

            List<PdfItem> rootFiles = new ArrayList<>();

            for (File item : items) {

                if (item.isDirectory()) {
                    List<PdfItem> files = new ArrayList<>();
                    scanPdf(item, rootPath + "/" + main + "/" + item.getName(), files);
                    subMap.put(item.getName(), files);
                }

                if (item.isFile() && item.getName().toLowerCase().endsWith(".pdf")) {
                    rootFiles.add(new PdfItem(
                            item.getName(),
                            "/" + rootPath + "/" + main + "/" + item.getName()
                    ));
                }
            }

            if (!rootFiles.isEmpty()) {
                subMap.put("__FILES__", rootFiles);
            }

            result.put(main, subMap);
        }

        return result;
    }

    private void scanPdf(File dir, String webPath, List<PdfItem> collector) {

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(".pdf")) {
                collector.add(new PdfItem(
                        f.getName(),
                        "/" + webPath + "/" + f.getName()
                ));
            }
        }
    }
}