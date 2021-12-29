package com.kamijoucen.ruler.parse.impl;

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
