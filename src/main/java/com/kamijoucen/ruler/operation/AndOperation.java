package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class AndOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context, NodeVisitor visitor, BaseValue... params) {
        BaseValue lValue = lhs.eval(visitor);
        if (lValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("The '&&' operation is not supported for this value: " + lValue);
        }
        if (!((BoolValue) lValue).getValue()) {
            return BoolValue.get(false);
        }
        BaseValue rValue = rhs.eval(visitor);
        if (rValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("The '&&' operation is not supported for this value: " + rValue);
        }
        return rValue;
    }
}
