package com.example.web_app.controller.web;

import com.example.web_app.model.PdfItem;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hrd")
public class HrdController {

    @GetMapping
    public String hrdPage(Model model) throws IOException {

        model.addAttribute("primary", "FIG5");
        model.addAttribute("secondary", "HRD");

        model.addAttribute(
            "sidebar",
            readSidebarTwoLevel("pdfs/FIG5/HRD")
        );

        return "hrd";
    }

    /* =====================================
       SIDEBAR 2 LEVEL (COCOK DENGAN JS LAMA)
       ===================================== */
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

            // Jika folder utama tidak ada
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

            // FILE langsung di folder utama
            List<PdfItem> rootFiles = new ArrayList<>();

            for (File item : items) {

                // SUBFOLDER (CUSTOM / IMPORT / EXPORT)
                if (item.isDirectory()) {
                    List<PdfItem> files = new ArrayList<>();
                    scanPdf(item, rootPath + "/" + main + "/" + item.getName(), files);
                    subMap.put(item.getName(), files);
                }

                // FILE LANGSUNG
                if (item.isFile() && item.getName().toLowerCase().endsWith(".pdf")) {
                    rootFiles.add(new PdfItem(
                        item.getName(),
                        "/" + rootPath + "/" + main + "/" + item.getName()
                    ));
                }
            }

            // File langsung masuk key khusus
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