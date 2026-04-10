package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class OrOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
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
