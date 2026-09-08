package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

public class DefaultParamValNode extends AbstractBaseNode {

    private final NameNode name;
    private final BaseNode exp;

    public DefaultParamValNode(NameNode name, BaseNode exp, TokenLocation location) {
        super(location);
        this.name = name;
        this.exp = exp;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public NameNode getName() {
        return name;
    }


    public BaseNode getExp() {
        return exp;
    }

}
