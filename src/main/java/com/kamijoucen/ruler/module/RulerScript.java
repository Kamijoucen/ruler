package com.kamijoucen.ruler.module;

public class RulerScript {

    private String fileName;
    private String content;

    public RulerScript() {
    }

    public RulerScript(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
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
