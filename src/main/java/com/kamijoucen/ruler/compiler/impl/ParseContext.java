package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.common.NodeVisitor;

public class ParseContext {

    private NodeVisitor typeCheckVisitor;

    public ParseContext(NodeVisitor typeCheckVisitor) {
        this.typeCheckVisitor = typeCheckVisitor;
    }

    public NodeVisitor getTypeCheckVisitor() {
        return typeCheckVisitor;
    }

}
