package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

public class InfixDefinitionNode extends AbstractBaseNode {

    private final ClosureDefineNode function;

    public InfixDefinitionNode(ClosureDefineNode functionNode, TokenLocation location) {
        super(location);
        this.function = functionNode;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public ClosureDefineNode getFunction() {
        return function;
    }
}
