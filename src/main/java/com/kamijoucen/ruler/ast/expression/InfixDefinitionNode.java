package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class InfixDefinitionNode extends AbstractBaseNode {

    private final ClosureDefineNode function;

    public InfixDefinitionNode(ClosureDefineNode functionNode, TokenLocation location) {
        super(location);
        this.function = functionNode;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(RuntimeContext context, Scope scope) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public ClosureDefineNode getFunction() {
        return function;
    }
}
