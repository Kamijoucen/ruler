package com.kamijoucen.ruler.domain.ast;
import com.kamijoucen.ruler.domain.type.RulerType;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class VariableDefineNode extends BinaryOperationNode {

    public VariableDefineNode(BaseNode lhs, BaseNode rhs, TokenLocation location) {
        super(TokenType.ASSIGN, TokenType.ASSIGN.name(), lhs, rhs, null, location);
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public RulerType typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

}
