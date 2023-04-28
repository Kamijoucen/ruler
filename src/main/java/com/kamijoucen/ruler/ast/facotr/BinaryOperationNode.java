package com.kamijoucen.ruler.ast.facotr;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;

public class BinaryOperationNode extends AbstractBaseNode {

    private final BaseNode lhs;

    private final BaseNode rhs;

    private final TokenType op;

    private final String operationName;

    public BinaryOperationNode(TokenType op, String operationName, BaseNode lhs, BaseNode rhs, TokenLocation location) {
        super(location);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
        this.operationName = operationName;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(RuntimeContext context, Scope scope) {
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


    public String getOperationName() {
        return operationName;
    }

}
