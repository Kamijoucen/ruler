package com.kamijoucen.ruler.module;

public class RulerScript {

    private String path;

    private String fileName;

    private String content;

    public RulerScript() {
    }

    public RulerScript(String path, String fileName, String content) {
        this.path = path;
        this.fileName = fileName;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
