package com.kamijoucen.ruler.domain.ast.factor;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.List;

public class StringInterpolationNode extends AbstractBaseNode {

    private List<BaseNode> parts;

    public StringInterpolationNode(List<BaseNode> parts, TokenLocation location) {
        super(location);
        this.parts = parts;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public List<BaseNode> getParts() {
        return parts;
    }

    public void setParts(List<BaseNode> parts) {
        this.parts = parts;
    }
}
