package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.token.TokenType;

public class IndexNode extends BinaryOperationNode {

    public IndexNode(BaseNode lhs, BaseNode rhs, TokenLocation location) {
        super(TokenType.INDEX, TokenType.INDEX.name(), lhs, rhs, location);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }
}
