package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class UnaryOperationNode extends AbstractBaseNode {

    private final TokenType op;
    private final BaseNode exp;
    private final BinaryOperation operation;

    public UnaryOperationNode(TokenType op, BaseNode exp, BinaryOperation operation, TokenLocation location) {
        super(location);
        this.op = op;
        this.exp = exp;
        this.operation = operation;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public TokenType getOp() {
        return op;
    }

    public BaseNode getExp() {
        return exp;
    }

    public BinaryOperation getOperation() {
        return operation;
    }
}
