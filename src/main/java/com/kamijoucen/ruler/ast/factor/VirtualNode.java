package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class VirtualNode implements BaseNode {

    private final BaseValue baseValue;

    public VirtualNode(BaseValue baseValue) {
        this.baseValue = baseValue;
    }


    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return baseValue;
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TokenLocation getLocation() {
        throw new UnsupportedOperationException();
    }

    public BaseValue getBaseValue() {
        return baseValue;
    }
}
