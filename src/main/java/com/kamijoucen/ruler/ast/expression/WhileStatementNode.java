package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class WhileStatementNode extends AbstractBaseNode {

    private BaseNode condition;
    private BaseNode block;

    public WhileStatementNode(BaseNode condition, BaseNode block, TokenLocation location) {
        super(location);
        this.condition = condition;
        this.block = block;
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

    public BaseNode getBlock() {
        return block;
    }

    public void setBlock(BaseNode block) {
        this.block = block;
    }
}
