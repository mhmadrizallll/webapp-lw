package com.example.web_app.model;

public class PdfItem {
    private String name;
    private String path;

    public PdfItem(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() { return name; }
    public String getPath() { return path; }
}