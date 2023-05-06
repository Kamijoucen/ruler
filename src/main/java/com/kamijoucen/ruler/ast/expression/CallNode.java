package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallNode extends BinaryOperationNode {

    private final List<BaseNode> params;

    // todo
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
