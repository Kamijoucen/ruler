package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ForEachStatementNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.ValueType;

import java.util.List;

public class ForEachStatementEval implements BaseEval<ForEachStatementNode> {

    @Override
    public BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        BaseValue listValue = EvalVisitor.evaluate(node.getList(), scope, context);
        if (listValue.getType() != ValueType.ARRAY) {
            throw new RulerRuntimeException("for-each requires an array",
                    node.getList().getLocation());
        }
        List<BaseValue> arrayValues = ((ArrayValue) listValue).getValues();
        Token loopName = node.getLoopName();
        BaseNode block = node.getBlock();

        int maxLoopNumber = context.getConfiguration().getMaxLoopNumber();
        int loopCount = 0;

        Scope forScope = new Scope("for each scope", false, scope, null);

        BaseValue lastValue = NullValue.INSTANCE;
        for (BaseValue baseValue : arrayValues) {
            if (maxLoopNumber > 0 && ++loopCount > maxLoopNumber) {
                throw new RulerRuntimeException("Loop count exceeded! max: " + maxLoopNumber,
                        node.getLocation());
            }
            forScope.putLocal(loopName.name, baseValue);
            lastValue = EvalVisitor.evaluate(block, forScope, context);
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
}
