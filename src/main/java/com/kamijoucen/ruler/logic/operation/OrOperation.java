package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.logic.eval.EvalVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.exception.IllegalOperationException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class OrOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = EvalVisitor.evaluate(lhs, scope, context);
        if (lValue.getType() != ValueType.BOOL) {
            throw new IllegalOperationException("'||' not supported for: " + lValue);
        }
        if (((BoolValue) lValue).getValue()) {
            return BoolValue.get(true);
        }
        BaseValue rValue = EvalVisitor.evaluate(rhs, scope, context);
        if (rValue.getType() != ValueType.BOOL) {
            throw new IllegalOperationException("'||' not supported for: " + rValue);
        }
        return rValue;
    }
}
