package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.value.BaseValue;

public class VirtualNode implements BaseNode {

    private final BaseValue baseValue;

    public VirtualNode(BaseValue baseValue) {
        this.baseValue = baseValue;
    }


    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    @Override
    public TokenLocation getLocation() {
        return null;
    }

    public BaseValue getBaseValue() {
        return baseValue;
    }
}
