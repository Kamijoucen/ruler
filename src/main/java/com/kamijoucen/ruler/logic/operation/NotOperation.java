package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class NotOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue value = lhs.eval(scope, context);
        if (value.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("The '!' operation is not supported for this value: " + value);
        }
        return BoolValue.get(!((BoolValue) value).getValue());
    }
}
