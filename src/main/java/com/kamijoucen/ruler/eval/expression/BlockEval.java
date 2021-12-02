package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NoneValue;

import java.util.List;

public class BlockEval implements BaseEval<BlockNode> {
    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        Scope blockScope = new Scope("block", scope);
        List<BaseNode> blocks = node.getBlocks();
        for (BaseNode block : blocks) {
            BaseValue val = block.eval(context, blockScope);
            if (ValueType.RETURN == val.getType()) {
                return val;
            }
        }
        return NoneValue.INSTANCE;
    }
}
