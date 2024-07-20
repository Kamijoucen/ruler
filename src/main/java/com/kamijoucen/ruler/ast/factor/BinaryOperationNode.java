package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class BinaryOperationNode extends AbstractBaseNode {

    private final BaseNode lhs;

    private final BaseNode rhs;

    private final TokenType op;

    private final String opName;

    private final BinaryOperation operation;

    public BinaryOperationNode(TokenType op, String opName,
                               BaseNode lhs, BaseNode rhs, BinaryOperation operation, TokenLocation location) {
        super(location);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
        this.opName = opName;
        this.operation = operation;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public BaseNode getLhs() {
        return lhs;
    }


    public BaseNode getRhs() {
        return rhs;
    }


    public TokenType getOp() {
        return op;
    }


    public String getOpName() {
        return opName;
    }

    public BinaryOperation getOperation() {
        return operation;
    }
}
