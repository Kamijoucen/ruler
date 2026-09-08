package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.logic.eval.CallLogic;
import com.kamijoucen.ruler.logic.eval.EvalVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class CallOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
                            BaseValue... params) {
        BaseValue callable = EvalVisitor.evaluate(lhs, scope, context);
        return CallLogic.callValue(callable, scope, context, lhs.getLocation(), params);
    }
}
