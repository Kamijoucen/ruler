package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.logic.eval.EvalVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.exception.IllegalOperationException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class NotOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue value = EvalVisitor.evaluate(lhs, scope, context);
        if (value.getType() != ValueType.BOOL) {
            throw new IllegalOperationException("'!' not supported for: " + value);
        }
        return BoolValue.get(!((BoolValue) value).getValue());
    }
}
