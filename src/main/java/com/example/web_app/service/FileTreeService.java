package com.example.web_app.service;

import com.example.web_app.model.SidebarNode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileTreeService {

    private final String BASE_DIR = "../";

    public List<SidebarNode> buildTree(String rootPath) {

        List<SidebarNode> nodes = new ArrayList<>();

        File rootDir = new File(BASE_DIR + rootPath);

        if (!rootDir.exists() || !rootDir.isDirectory())
            return nodes;

        File[] files = rootDir.listFiles();

        if (files == null)
            return nodes;

        for (File file : files) {

            if (file.isDirectory()) {

                SidebarNode folder =
                        new SidebarNode(file.getName(), true, null);

                folder.getChildren().addAll(
                        buildTree(rootPath + "/" + file.getName())
                );

                nodes.add(folder);

            } else if (file.isFile() && isSupported(file.getName())) {

                nodes.add(
                        new SidebarNode(
                                file.getName(),
                                false,
                                rootPath + "/" + file.getName()
                        )
                );
            }
        }

        return nodes;
    }

    private boolean isSupported(String name) {

        String lower = name.toLowerCase();

        return lower.endsWith(".pdf")
                || lower.endsWith(".doc")
                || lower.endsWith(".docx")
                || lower.endsWith(".xls")
                || lower.endsWith(".xlsx")
                || lower.endsWith(".ppt")
                || lower.endsWith(".pptx");
    }
}