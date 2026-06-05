package com.kamijoucen.ruler.domain.ast.expression;
import com.kamijoucen.ruler.domain.type.RulerType;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.ast.factor.StringNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class RuleStatementNode extends AbstractBaseNode {

    private StringNode alias;
    private BlockNode block;

    public RuleStatementNode(StringNode alias, BlockNode block, TokenLocation location) {
        super(location);
        this.alias = alias;
        this.block = block;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public RulerType typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
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
