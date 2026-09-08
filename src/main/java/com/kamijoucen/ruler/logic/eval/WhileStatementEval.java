package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.WhileStatementNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class WhileStatementEval implements BaseEval<WhileStatementNode> {

    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        BaseNode block = node.getBlock();
        int maxLoopNumber = context.getConfiguration().getMaxLoopNumber();
        int loopCount = 0;
        BaseValue lastValue = NullValue.INSTANCE;
        while (evalCondition(node, scope, context)) {
            if (maxLoopNumber > 0 && ++loopCount > maxLoopNumber) {
                throw new RulerRuntimeException("Loop count exceeded! max: " + maxLoopNumber,
                        node.getLocation());
            }
            lastValue = EvalVisitor.evaluate(block, scope, context);
            if (context.isReturnFlag()) {
                break;
            } else if (context.consumeBreakFlag()) {
                break;
            } else if (context.consumeContinueFlag()) {
                continue;
            }
        }
        return lastValue;
    }

    private boolean evalCondition(WhileStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue conditionValue = EvalVisitor.evaluate(node.getCondition(), scope, context);
        if (conditionValue.getType() != ValueType.BOOL) {
            throw new RulerRuntimeException("while condition must be boolean",
                    node.getCondition().getLocation());
        }
        return ((BoolValue) conditionValue).getValue();
    }

}
