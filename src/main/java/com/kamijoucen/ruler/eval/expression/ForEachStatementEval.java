package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ForEachStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.QuadConsumer;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.LoopCountCheckOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.List;

public class ForEachStatementEval implements BaseEval<ForEachStatementNode> {

    private final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> checkLoopNumberEval =
            LoopCountCheckOperation::accept;

    private final QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> blankEval =
            (operation, node, scope, context) -> {
            };

    @Override
    public BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue listValue = node.getList().eval(scope, context);
        if (listValue.getType() != ValueType.ARRAY) {
            throw SyntaxException.withSyntax("The value of the expression must be an array!");
        }
        List<BaseValue> arrayValues = ((ArrayValue) listValue).getValues();
        Token loopName = node.getLoopName();
        BaseNode block = node.getBlock();

        LoopCountCheckOperation loopCountCheckOperation = null;
        QuadConsumer<LoopCountCheckOperation, BaseNode, Scope, RuntimeContext> check;
        if (context.getConfiguration().getMaxLoopNumber() > 0) {
            loopCountCheckOperation = context.getConfiguration().getRuntimeBehaviorFactory()
                    .createLoopCountCheckOperation();
            check = checkLoopNumberEval;
        } else {
            check = blankEval;
        }

        Scope forScope = new Scope("for each scope", false, scope, null);

        BaseValue lastValue = NullValue.INSTANCE;
        for (BaseValue baseValue : arrayValues) {
            check.accept(loopCountCheckOperation, block, forScope, context);
            forScope.putLocal(loopName.name, baseValue);
            lastValue = block.eval(forScope, context);
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
