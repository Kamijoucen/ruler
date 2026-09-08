package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.logic.eval.EvalVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.logic.property.PropertyAccessor;

public class IndexOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lVal = EvalVisitor.evaluate(lhs, scope, context);
        BaseValue idx = EvalVisitor.evaluate(rhs, scope, context);
        return PropertyAccessor.getIndexProperty(lVal, idx, context);
    }

}
