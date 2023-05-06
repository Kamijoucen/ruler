package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class AndOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        if (lValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("该值不支持&&:" + lValue);
        }
        if (!((BoolValue) lValue).getValue()) {
            return BoolValue.get(false);
        }
        BaseValue rValue = rhs.eval(scope, context);
        if (rValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("该值不支持&&:" + rValue);
        }
        if (!((BoolValue) rValue).getValue()) {
            return BoolValue.get(false);
        }
        return BoolValue.get(true);
    }
}
