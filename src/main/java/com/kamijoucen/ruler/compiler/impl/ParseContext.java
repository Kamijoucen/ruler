package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.config.RulerConfiguration;

public class ParseContext {

    private final RulerConfiguration configuration;

    private boolean isRoot;

    private boolean inLoop;


    public ParseContext(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public boolean isInLoop() {
        return inLoop;
    }

    public void setInLoop(boolean inLoop) {
        this.inLoop = inLoop;
    }

}
