package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

public class RuleStatementNode extends AbstractBaseNode {

    private StringNode alias;
    private BlockNode block;

    public RuleStatementNode(StringNode alias, BlockNode block, TokenLocation location) {
        super(location);
        this.alias = alias;
        this.block = block;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
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
