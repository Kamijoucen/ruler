package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.List;

public class BlockEval implements BaseEval<BlockNode> {
    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        Scope blockScope = new Scope("block", scope);
        List<BaseNode> blocks = node.getBlocks();

        BaseValue lastVal = NullValue.INSTANCE;
        for (BaseNode block : blocks) {
            lastVal = block.eval(context, blockScope);
            if (ValueType.RETURN == lastVal.getType()) {
                return lastVal;
            }
        }
        return lastVal;
    }
}
