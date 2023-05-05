package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;

public class UnarySubOperation implements BinaryOperation {
    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue value = params[0];
        if (value.getType() == ValueType.INTEGER) {
            IntegerValue val = (IntegerValue) value;
            return context.getConfiguration().getIntegerNumberCache().getValue(-val.getValue());
        } else if (value.getType() == ValueType.DOUBLE) {
            DoubleValue val = (DoubleValue) value;
            return new DoubleValue(-val.getValue());
        } else {
            throw new RuntimeException("该值不支持取负数:" + Arrays.toString(params));
        }
    }
}
