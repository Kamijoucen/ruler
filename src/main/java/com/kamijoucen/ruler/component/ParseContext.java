package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.type.RulerType;

public class ParseContext {

    private final RulerConfiguration configuration;

    private NodeVisitor<RulerType> typeCheckVisitor;

    private boolean isRoot;

    private boolean inLoop;

    public ParseContext(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public NodeVisitor<RulerType> getTypeCheckVisitor() {
        return typeCheckVisitor;
    }

    public void setTypeCheckVisitor(NodeVisitor<RulerType> typeCheckVisitor) {
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
