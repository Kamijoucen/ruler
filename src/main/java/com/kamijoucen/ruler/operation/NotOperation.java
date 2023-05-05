package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class NotOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue tempExpVal = lhs.eval(scope, context);
        if (tempExpVal.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("该值不支持'!'操作:" + tempExpVal);
        }
        BoolValue expVal = (BoolValue) tempExpVal;
        return BoolValue.get(!expVal.getValue());
    }
}
