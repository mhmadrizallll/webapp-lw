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
@RequestMapping("/exim")
public class EximController {

  @GetMapping
  public String itPage(Model model) throws IOException {
    model.addAttribute("primary", "FIG5");
    model.addAttribute("secondary", "EXIM");

    // Baca sidebar dari folder "pdfs/FIG5/EXIM"
    model.addAttribute("sidebar", readSidebar("pdfs/FIG5/EXIM"));

    return "exim"; // templates/exim.html
  }

  /* ======== helper ======== */

  private Map<String, List<PdfItem>> readSidebar(String rootPath)
    throws IOException {
    Map<String, List<PdfItem>> result = new LinkedHashMap<>();

    // Folder utama wajib ada
    String[] folders = {
      "CHART ORG",
      "FROM",
      "JOB DESC",
      "PROCEDURE",
      "STANDARD",
    };

    for (String folderName : folders) {
      List<PdfItem> items = new ArrayList<>();

      Resource folderResource = new ClassPathResource(
        "static/" + rootPath + "/" + folderName
      );
      if (folderResource.exists()) {
        File folderFile = folderResource.getFile();
        items.addAll(
          listPdfsRecursively(folderFile, "/" + rootPath + "/" + folderName)
        );
      }

      result.put(folderName, items);
    }

    return result;
  }

  // Fungsi rekursif untuk membaca folder dan file PDF
  private List<PdfItem> listPdfsRecursively(File folder, String basePath) {
    List<PdfItem> items = new ArrayList<>();
    File[] files = folder.listFiles();
    if (files != null) {
      for (File f : files) {
        if (f.isDirectory()) {
          // Tambahkan folder sebagai item
          items.add(
            new PdfItem(f.getName(), basePath + "/" + f.getName(), true)
          );
          // Rekursi ke subfolder
          items.addAll(listPdfsRecursively(f, basePath + "/" + f.getName()));
        } else if (f.getName().toLowerCase().endsWith(".pdf")) {
          // Tambahkan file PDF
          items.add(
            new PdfItem(f.getName(), basePath + "/" + f.getName(), false)
          );
        }
      }
    }
    return items;
  }
}
