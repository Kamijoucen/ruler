package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.LoopCountCheckOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;
import com.kamijoucen.ruler.value.constant.ReturnValue;

public class WhileStatementEval implements BaseEval<WhileStatementNode> {
    @Override
    public EvalResult eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        BaseNode block = node.getBlock();
        LoopCountCheckOperation loopCountCheckOperation = context.getConfiguration()
                .getRuntimeBehaviorFactory().createLoopCountCheckOperation();
        BaseValue lastValue = NullValue.INSTANCE;
        while (((BoolValue) node.getCondition().eval(scope, context)).getValue()) {
            loopCountCheckOperation.accept(node, scope, context);
            lastValue = block.eval(scope, context);
            if (ValueType.BREAK == lastValue.getType()) {
                break;
            } else if (ValueType.RETURN == lastValue.getType()) {
                return ReturnValue.INSTANCE;
            }
        }
        if (lastValue.getType() == ValueType.BREAK) {
            return NullValue.INSTANCE;
        }

        return lastValue;
    }
}
