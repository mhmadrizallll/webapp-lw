package com.example.web_app.controller.web;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class FileDownloadController {

    private final String BASE_DIR = "../";

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String path) {

        try {

            if (path.contains("..")) {
                return ResponseEntity.badRequest().build();
            }

            File file = new File(BASE_DIR + path);

            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            String encodedFileName = URLEncoder.encode(
                    file.getName(),
                    StandardCharsets.UTF_8
            );

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + encodedFileName + "\""
                    )
                    .header(
                            HttpHeaders.CONTENT_TYPE,
                            "application/octet-stream"
                    )
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}