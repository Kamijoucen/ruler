package com.kamijoucen.ruler.domain.ast.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.logic.operation.BinaryOperation;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.List;

public class CallNode extends BinaryOperationNode {

    private final List<BaseNode> params;

    public CallNode(BaseNode lhs, BaseNode rhs, List<BaseNode> params, BinaryOperation operation, TokenLocation location) {
        super(TokenType.CALL, TokenType.CALL.name(), lhs, rhs, operation, location);
        this.params = params;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public List<BaseNode> getParams() {
        return params;
    }
}
