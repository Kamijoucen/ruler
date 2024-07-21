package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class OrOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        if (lValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("The '||' operation is not supported for this value: " + lValue);
        }
        if (((BoolValue) lValue).getValue()) {
            return BoolValue.get(true);
        }
        BaseValue rValue = rhs.eval(scope, context);
        if (rValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("The '||' operation is not supported for this value: " + rValue);
        }
        return rValue;
    }
}
