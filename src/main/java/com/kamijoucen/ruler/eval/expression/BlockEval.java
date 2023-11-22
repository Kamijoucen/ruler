package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

import java.util.List;

public class BlockEval implements BaseEval<BlockNode> {
    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        Scope blockScope = new Scope("block", false, scope, null);
        List<BaseNode> blocks = node.getBlocks();
        BaseValue lastVal = NullValue.INSTANCE;
        for (BaseNode block : blocks) {
            lastVal = block.eval(blockScope, context);
            if (context.isReturnFlag() || context.isBreakFlag()) {
                break;
            } else if (context.isContinueFlag()) {
                context.setContinueFlag(false);
                break;
            }
        }
        context.setCurrentSelfValue(null);
        return lastVal;
    }
}
