package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
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
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public List<BaseNode> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BaseNode> blocks) {
        this.blocks = blocks;
    }
}
