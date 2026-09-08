package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

public class TypeOfNode extends AbstractBaseNode {

    private BaseNode exp;

    public TypeOfNode(BaseNode exp, TokenLocation location) {
        super(location);
        this.exp = exp;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public BaseNode getExp() {
        return exp;
    }

    public void setExp(BaseNode exp) {
        this.exp = exp;
    }
}
