package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class FunctionDefineNode implements BaseNode {

    private String name;

    private List<String> param;

    private BaseNode block;

    public FunctionDefineNode(String name, List<String> param, BaseNode block) {
        this.name = name;
        this.param = param;
        this.block = block;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
