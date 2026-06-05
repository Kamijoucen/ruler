package com.kamijoucen.ruler.domain.ast.factor;
import com.kamijoucen.ruler.domain.type.RulerType;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.logic.operation.BinaryOperation;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.value.BaseValue;

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
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public RulerType typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
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
