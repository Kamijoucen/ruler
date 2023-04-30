package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class BlockNode extends AbstractBaseNode {

    private List<BaseNode> blocks;

    public BlockNode(List<BaseNode> blocks, TokenLocation location) {
        super(location);
        this.blocks = blocks;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public List<BaseNode> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BaseNode> blocks) {
        this.blocks = blocks;
    }
}
