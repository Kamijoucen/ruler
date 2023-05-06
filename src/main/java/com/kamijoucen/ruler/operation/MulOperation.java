package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;

public class MulOperation implements BinaryOperation {
    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        if (lValue.getType() == ValueType.INTEGER && rValue.getType() == ValueType.INTEGER) {
            IntegerValue val1 = (IntegerValue) lValue;
            IntegerValue val2 = (IntegerValue) rValue;
            return context.getConfiguration().getIntegerNumberCache().getValue(val1.getValue() * val2.getValue());
        } else if (lValue.getType() == ValueType.INTEGER && rValue.getType() == ValueType.DOUBLE) {
            IntegerValue val1 = (IntegerValue) lValue;
            DoubleValue val2 = (DoubleValue) rValue;
            return new DoubleValue(val1.getValue() * val2.getValue());
        } else if (lValue.getType() == ValueType.DOUBLE && rValue.getType() == ValueType.INTEGER) {
            DoubleValue val1 = (DoubleValue) lValue;
            IntegerValue val2 = (IntegerValue) rValue;
            return new DoubleValue(val1.getValue() * val2.getValue());
        } else if (lValue.getType() == ValueType.DOUBLE && rValue.getType() == ValueType.DOUBLE) {
            DoubleValue val1 = (DoubleValue) lValue;
            DoubleValue val2 = (DoubleValue) rValue;
            return new DoubleValue(val1.getValue() * val2.getValue());
        } else {
            throw SyntaxException.withSyntax("该值不支持做乘法:" + Arrays.toString(params));
        }
    }
}
