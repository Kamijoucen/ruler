package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.token.TokenType;

public class BinaryOperationNode extends AbstractBaseNode {

    private final BaseNode lhs;

    private final BaseNode rhs;

    private final TokenType op;

    private final String opName;


    public BinaryOperationNode(TokenType op, String opName,
                               BaseNode lhs, BaseNode rhs, TokenLocation location) {
        super(location);
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
        this.opName = opName;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
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

}
