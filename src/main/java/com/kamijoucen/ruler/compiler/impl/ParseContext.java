package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.RulerConfiguration;

public class ParseContext {

    private final RulerConfiguration configuration;

    private NodeVisitor typeCheckVisitor;

    private boolean isRoot;

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
}
