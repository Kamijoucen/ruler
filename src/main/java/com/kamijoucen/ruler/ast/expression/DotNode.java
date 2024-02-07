package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class DotNode extends BinaryOperationNode {

    public DotNode(BaseNode lhs, BaseNode rhs, BinaryOperation operation, TokenLocation location) {
        super(TokenType.DOT, TokenType.DOT.name(), lhs, rhs, operation, location);
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }
}
