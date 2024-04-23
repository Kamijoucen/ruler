package com.kamijoucen.ruler.config.impl;

public class StartConfig {

    private String filePath;

    private int maxLoopNumber;

    private int maxStackDepth;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getMaxLoopNumber() {
        return maxLoopNumber;
    }

    public void setMaxLoopNumber(int maxLoopNumber) {
        this.maxLoopNumber = maxLoopNumber;
    }

    public int getMaxStackDepth() {
        return maxStackDepth;
    }

    public void setMaxStackDepth(int maxStackDepth) {
        this.maxStackDepth = maxStackDepth;
    }

}
