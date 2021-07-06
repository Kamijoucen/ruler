package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;

import java.util.List;

public class ClosureValue implements BaseValue {

    private Scope defineScope;

    private List<BaseNode> param;

    private BaseNode block;

    public ClosureValue(Scope defineScope, List<BaseNode> param, BaseNode block) {
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
