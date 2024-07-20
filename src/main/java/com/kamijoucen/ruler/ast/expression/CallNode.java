package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallNode extends BinaryOperationNode {

    private final List<BaseNode> params;

    public CallNode(BaseNode lhs, BaseNode rhs, List<BaseNode> params, BinaryOperation operation, TokenLocation location) {
        super(TokenType.CALL, TokenType.CALL.name(), lhs, rhs, operation, location);
        this.params = params;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public List<BaseNode> getParams() {
        return params;
    }
}
