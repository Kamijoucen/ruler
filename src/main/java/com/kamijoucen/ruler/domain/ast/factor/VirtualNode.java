package com.kamijoucen.ruler.domain.ast.factor;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

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
