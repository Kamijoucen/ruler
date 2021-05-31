package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class ClosureNode implements BaseNode {

    private List<String> param;

    private BaseNode block;

    public ClosureNode(List<String> param, BaseNode block) {
        this.param = param;
        this.block = block;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }

    public List<String> getParam() {
        return param;
    }

    public void setParam(List<String> param) {
        this.param = param;
    }

    public BaseNode getBlock() {
        return block;
    }

    public void setBlock(BaseNode block) {
        this.block = block;
    }
}
