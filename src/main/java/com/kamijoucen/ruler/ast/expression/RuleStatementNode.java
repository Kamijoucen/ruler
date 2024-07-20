package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.factor.StringNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class RuleStatementNode extends AbstractBaseNode {

    private StringNode alias;
    private BlockNode block;

    public RuleStatementNode(StringNode alias, BlockNode block, TokenLocation location) {
        super(location);
        this.alias = alias;
        this.block = block;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public StringNode getAlias() {
        return alias;
    }

    public void setAlias(StringNode alias) {
        this.alias = alias;
    }

    public BlockNode getBlock() {
        return block;
    }

    public void setBlock(BlockNode block) {
        this.block = block;
    }
}
