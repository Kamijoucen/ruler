package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class LoopBlockNode extends AbstractBaseNode {

    private List<BaseNode> blocks;

    public LoopBlockNode(List<BaseNode> blocks) {
        this.blocks = blocks;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope, context);
    }

    public List<BaseNode> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BaseNode> blocks) {
        this.blocks = blocks;
    }
}
