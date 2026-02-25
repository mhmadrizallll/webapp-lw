package com.example.web_app.model;

public class PdfItem {

  private String name;
  private String path;
  private boolean isFolder;

  public PdfItem(String name, String path, boolean isFolder) {
    this.name = name;
    this.path = path;
    this.isFolder = isFolder;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public boolean isFolder() {
    return isFolder;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setFolder(boolean folder) {
    isFolder = folder;
  }
}
