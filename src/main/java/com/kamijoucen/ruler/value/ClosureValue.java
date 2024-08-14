package com.kamijoucen.ruler.value;

import java.util.List;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.Scope;

public class ClosureValue extends AbstractValue {

    private String name;
    private Scope defineScope;
    private List<BaseNode> param;
    private BaseNode block;

    public ClosureValue(String name, Scope defineScope, List<BaseNode> param, BaseNode block) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
