package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class StringAddOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        return new StringValue(lValue.toString() + rValue.toString());
    }

}
