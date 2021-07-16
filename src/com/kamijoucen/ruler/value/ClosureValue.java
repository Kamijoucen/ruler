package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.runtime.ContextScope;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.List;

public class ClosureValue implements BaseValue {

    private RStack<ContextScope> defineScope;

    private ContextScope closureScope;

    private List<BaseNode> param;

    private BaseNode block;

    public ClosureValue(RStack<ContextScope> defineScope, List<BaseNode> param, BaseNode block) {
        this.defineScope = defineScope;
        this.param = param;
        this.block = block;
    }

    @Override
    public ValueType getType() {
        return ValueType.CLOSURE;
    }

    public RStack<ContextScope> getDefineScope() {
        return defineScope;
    }

    public void setDefineScope(RStack<ContextScope> defineScope) {
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
