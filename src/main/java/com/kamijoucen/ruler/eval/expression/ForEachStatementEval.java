package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ForEachStatementNode;
import com.kamijoucen.ruler.common.BaseEval;
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

    @Override
    public BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue listValue = node.getList().eval(scope, context);
        if (listValue.getType() != ValueType.ARRAY) {
            throw SyntaxException.withSyntax("The value of the expression must be an array!");
        }
        List<BaseValue> arrayValues = ((ArrayValue) listValue).getValues();
        Token loopName = node.getLoopName();
        BaseNode block = node.getBlock();

        LoopCountCheckOperation loopCountCheckOperation = context.getConfiguration()
                .getRuntimeBehaviorFactory().createLoopCountCheckOperation();

        Scope forScope = new Scope("for each scope", false, scope, null);

        BaseValue lastValue = NullValue.INSTANCE;
        for (BaseValue baseValue : arrayValues) {
            loopCountCheckOperation.accept(node, scope, context);

            forScope.putLocal(loopName.name, baseValue);

            lastValue = block.eval(forScope, context);
            if (context.isReturnFlag()) {
                break;
            } else if (context.isBreakFlag()) {
                context.setBreakFlag(false);
                break;
            }
        }
        return lastValue;
    }
}
