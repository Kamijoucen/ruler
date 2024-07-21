package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class NotOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context, BaseValue... params) {
        BaseValue value = lhs.eval(scope, context);
        if (value.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("The '!' operation is not supported for this value: " + value);
        }
        return BoolValue.get(!((BoolValue) value).getValue());
    }
}
