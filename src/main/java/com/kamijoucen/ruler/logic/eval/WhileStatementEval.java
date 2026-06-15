package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.WhileStatementNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.common.QuadConsumer;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.LoopCountCheckOperation;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class WhileStatementEval implements BaseEval<WhileStatementNode> {

    private final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> checkLoopNumberEval =
            LoopCountCheckOperation::accept;

    private final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> blankEval =
            (operation, node, scope, context) -> {
            };


    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        BaseNode block = node.getBlock();
        LoopCountCheckOperation loopCountCheckOperation = null;
        QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> check;
        if (context.getConfiguration().getMaxLoopNumber() > 0) {
            loopCountCheckOperation = new LoopCountCheckOperation();
            check = checkLoopNumberEval;
        } else {
            check = blankEval;
        }
        BaseValue lastValue = NullValue.INSTANCE;
        while (evalCondition(node, scope, context)) {
            check.accept(loopCountCheckOperation, node, scope, context);
            lastValue = block.eval(scope, context);
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
        BaseValue conditionValue = node.getCondition().eval(scope, context);
        if (conditionValue.getType() != ValueType.BOOL) {
            throw new RulerRuntimeException("while condition must be boolean",
                    node.getCondition().getLocation());
        }
        return ((BoolValue) conditionValue).getValue();
    }

}
