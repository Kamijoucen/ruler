package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.token.TokenType;

import java.util.List;

public class CallNode extends BinaryOperationNode {

    private final List<BaseNode> params;

    public CallNode(BaseNode lhs, List<BaseNode> params, TokenLocation location) {
        super(TokenType.CALL, TokenType.CALL.name(), lhs, null, location);
        this.params = params;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public List<BaseNode> getParams() {
        return params;
    }
}
