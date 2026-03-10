package com.example.web_app.model;

import java.util.ArrayList;
import java.util.List;

public class SidebarNode {

    private String name;
    private boolean folder;
    private String path;
    private List<SidebarNode> children = new ArrayList<>();

    public SidebarNode(String name, boolean folder, String path) {
        this.name = name;
        this.folder = folder;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public boolean isFolder() {
        return folder;
    }

    public String getPath() {
        return path;
    }

    public List<SidebarNode> getChildren() {
        return children;
    }

    public void addChild(SidebarNode node) {
        children.add(node);
    }
}