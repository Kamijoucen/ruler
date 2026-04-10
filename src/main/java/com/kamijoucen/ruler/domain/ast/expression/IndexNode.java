package com.kamijoucen.ruler.domain.ast.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.logic.operation.BinaryOperation;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class IndexNode extends BinaryOperationNode {

    public IndexNode(BaseNode lhs, BaseNode rhs, BinaryOperation operation, TokenLocation location) {
        super(TokenType.INDEX, TokenType.INDEX.name(), lhs, rhs, operation, location);
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
