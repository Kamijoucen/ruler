package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.logic.eval.CallLogic;
import com.kamijoucen.ruler.logic.eval.EvalVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class CustomOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
                            BaseValue... params) {
        BaseValue left = EvalVisitor.evaluate(lhs, scope, context);
        BaseValue right = EvalVisitor.evaluate(rhs, scope, context);
        return CallLogic.callValue(params[0], scope, context, left, right);
    }
}
