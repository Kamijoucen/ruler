package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class UnaryAddOperation implements BinaryOperation {
    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        return params[0];
    }
}
