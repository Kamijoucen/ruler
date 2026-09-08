package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;

import java.util.List;

public class BlockEval implements BaseEval<BlockNode> {

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        Scope blockScope = new Scope("block", false, scope, null);
        List<BaseNode> blocks = node.getBlocks();
        BaseValue lastVal = NullValue.INSTANCE;
        for (BaseNode block : blocks) {
            lastVal = EvalVisitor.evaluate(block, blockScope, context);
            if (context.isReturnFlag() || context.isBreakFlag() || context.isContinueFlag()) {
                break;
            }
        }
        return lastVal;
    }
}
