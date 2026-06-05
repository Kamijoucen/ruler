package com.kamijoucen.ruler.domain.ast.expression;
import com.kamijoucen.ruler.domain.type.RulerType;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class InfixDefinitionNode extends AbstractBaseNode {

    private final ClosureDefineNode function;

    public InfixDefinitionNode(ClosureDefineNode functionNode, TokenLocation location) {
        super(location);
        this.function = functionNode;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public RulerType typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public ClosureDefineNode getFunction() {
        return function;
    }
}
