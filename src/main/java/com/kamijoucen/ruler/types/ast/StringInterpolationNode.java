package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

import java.util.List;

public class StringInterpolationNode extends AbstractBaseNode {

    private List<BaseNode> parts;

    public StringInterpolationNode(List<BaseNode> parts, TokenLocation location) {
        super(location);
        this.parts = parts;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public List<BaseNode> getParts() {
        return parts;
    }

    public void setParts(List<BaseNode> parts) {
        this.parts = parts;
    }
}
