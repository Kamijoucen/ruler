package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.application.RulerConfiguration;

public class ParseContext {

    private final RulerConfiguration configuration;

    private NodeVisitor typeCheckVisitor;

    private boolean isRoot;

    private boolean inLoop;

    public ParseContext(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public NodeVisitor getTypeCheckVisitor() {
        return typeCheckVisitor;
    }

    public void setTypeCheckVisitor(NodeVisitor typeCheckVisitor) {
        this.typeCheckVisitor = typeCheckVisitor;
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
