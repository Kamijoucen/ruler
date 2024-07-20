package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class AssignNode extends BinaryOperationNode {

    public AssignNode(BaseNode lhs, BaseNode rhs, BinaryOperation operation, TokenLocation location) {
        super(TokenType.ASSIGN, TokenType.ASSIGN.name(), lhs, rhs, operation, location);
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }
}
