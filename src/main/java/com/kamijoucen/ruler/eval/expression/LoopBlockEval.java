package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.LoopBlockNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NoneValue;

import java.util.List;

public class LoopBlockEval implements BaseEval<LoopBlockNode> {
    @Override
    public BaseValue eval(LoopBlockNode node, Scope scope, RuntimeContext context) {
        Scope blockScope = new Scope("loop block", scope);
        if (scope.getCurrentLoopVariable() != null) {
            blockScope.putLocal(scope.getCurrentLoopVariableName(), scope.getCurrentLoopVariable());
        }
        List<BaseNode> blocks = node.getBlocks();

        BaseValue lastVal = null;
        for (BaseNode block : blocks) {
            lastVal = block.eval(context, blockScope);
            if (ValueType.CONTINUE == lastVal.getType()) {
                break;
            } else if (ValueType.BREAK == lastVal.getType()) {
                return lastVal;
            } else if (ValueType.RETURN == lastVal.getType()) {
                return lastVal;
            }
        }
        return lastVal;
    }
}
