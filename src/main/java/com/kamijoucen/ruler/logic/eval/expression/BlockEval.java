package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.BlockNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;

import java.util.List;

public class BlockEval implements BaseEval<BlockNode> {

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        Scope blockScope = new Scope("block", false, scope, null);
        List<BaseNode> blocks = node.getBlocks();
        BaseValue lastVal = NullValue.INSTANCE;
        for (BaseNode block : blocks) {
            lastVal = block.eval(blockScope, context);
            if (context.isReturnFlag() || context.isBreakFlag() || context.isContinueFlag()) {
                break;
            }
        }
        return lastVal;
    }
}
