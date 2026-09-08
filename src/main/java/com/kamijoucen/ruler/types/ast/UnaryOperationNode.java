package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.token.TokenType;

public class UnaryOperationNode extends AbstractBaseNode {

    private final TokenType op;
    private final BaseNode exp;

    public UnaryOperationNode(TokenType op, BaseNode exp, TokenLocation location) {
        super(location);
        this.op = op;
        this.exp = exp;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public TokenType getOp() {
        return op;
    }

    public BaseNode getExp() {
        return exp;
    }

}
