package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.QuadConsumer;
import com.kamijoucen.ruler.runtime.LoopCountCheckOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.NullValue;

public class WhileStatementEval implements BaseEval<WhileStatementNode> {

    private final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> checkLoopNumberEval =
            (operation, node, scope, context) -> {
                operation.accept(node, scope, context);
            };

    private final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> blankEval =
            (operation, node, scope, context) -> {
            };


    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        BaseNode block = node.getBlock();
        LoopCountCheckOperation loopCountCheckOperation = null;
        QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> check = null;
        if (context.getConfiguration().getMaxLoopNumber() > 0) {
            loopCountCheckOperation = context.getConfiguration().getRuntimeBehaviorFactory()
                    .createLoopCountCheckOperation();
            check = checkLoopNumberEval;
        } else {
            check = blankEval;
        }
        BaseValue lastValue = NullValue.INSTANCE;
        while (((BoolValue) node.getCondition().eval(scope, context)).getValue()) {
            check.accept(loopCountCheckOperation, node, scope, context);
            lastValue = block.eval(scope, context);
            if (context.isReturnFlag()) {
                break;
            } else if (context.isBreakFlag()) {
                context.setBreakFlag(false);
                break;
            } else if (context.isContinueFlag()) {
                context.setContinueFlag(false);
                continue;
            }
        }
        return lastValue;
    }

}
