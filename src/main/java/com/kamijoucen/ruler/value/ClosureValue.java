package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.List;

public class ClosureValue extends AbstractValue {

    private Scope defineScope;
    private List<BaseNode> param;
    private BaseNode block;

    public ClosureValue(Scope defineScope, List<BaseNode> param, BaseNode block, RClass rClass) {
        super(rClass);
        this.defineScope = defineScope;
        this.param = param;
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

    public List<BaseNode> getParam() {
        return param;
    }

    public void setParam(List<BaseNode> param) {
        this.param = param;
    }

    public BaseNode getBlock() {
        return block;
    }

    public void setBlock(BaseNode block) {
        this.block = block;
    }
}
