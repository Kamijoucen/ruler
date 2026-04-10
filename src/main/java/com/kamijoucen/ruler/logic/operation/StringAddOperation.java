package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class StringAddOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        return new StringValue(lValue.toString() + rValue.toString());
    }

}
