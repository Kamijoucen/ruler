package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class IfStatementNode implements BaseNode {

    private BaseNode condition;

    private BaseNode thenBlock;

    private BaseNode elseBlock;

    public IfStatementNode(BaseNode condition, BaseNode thenBlock, BaseNode elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope, context);
    }

    public BaseNode getCondition() {
        return condition;
    }

    public void setCondition(BaseNode condition) {
        this.condition = condition;
    }

    public BaseNode getThenBlock() {
        return thenBlock;
    }

    public void setThenBlock(BaseNode thenBlock) {
        this.thenBlock = thenBlock;
    }

    public BaseNode getElseBlock() {
        return elseBlock;
    }

    public void setElseBlock(BaseNode elseBlock) {
        this.elseBlock = elseBlock;
    }
}
