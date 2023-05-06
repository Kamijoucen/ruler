package com.kamijoucen.ruler.ast.facotr;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
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
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
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
