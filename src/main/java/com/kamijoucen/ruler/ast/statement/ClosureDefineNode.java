package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class ClosureDefineNode implements BaseNode {

    private String name;

    private List<BaseNode> param;

    private BaseNode block;

    public ClosureDefineNode(String name, List<BaseNode> param, BaseNode block) {
        this.name = name;
        this.param = param;
        this.block = block;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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