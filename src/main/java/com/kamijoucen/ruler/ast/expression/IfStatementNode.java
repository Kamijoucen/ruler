package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class IfStatementNode extends AbstractBaseNode {

    private BaseNode condition;
    private BaseNode thenBlock;
    private BaseNode elseBlock;

    public IfStatementNode(BaseNode condition, BaseNode thenBlock, BaseNode elseBlock, TokenLocation location) {
        super(location);
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
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
