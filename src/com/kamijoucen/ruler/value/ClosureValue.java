package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.env.Scope;

public class ClosureValue implements BaseValue {

    private Scope defineScope;

    private BaseNode block;

    public ClosureValue(Scope defineScope, BaseNode block) {
        this.defineScope = defineScope;
        this.block = block;
    }

    @Override
    public ValueType getType() {
        return ValueType.CLOSURE;
    }

    public Scope getDefineScope() {
        return defineScope;
    }

    public void setDefineScope(Scope defineScope) {
        this.defineScope = defineScope;
    }

    public BaseNode getBlock() {
        return block;
    }

    public void setBlock(BaseNode block) {
        this.block = block;
    }
}
