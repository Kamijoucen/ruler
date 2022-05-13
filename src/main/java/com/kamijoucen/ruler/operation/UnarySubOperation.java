package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;

public class UnarySubOperation implements Operation {
    @Override
    public BaseValue compute(RuntimeContext context, BaseValue... param) {
        BaseValue value = param[0];
        if (value.getType() == ValueType.INTEGER) {
            IntegerValue val = (IntegerValue) value;
            return new IntegerValue(-val.getValue());
        } else if (value.getType() == ValueType.DOUBLE) {
            DoubleValue val = (DoubleValue) value;
            return new DoubleValue(-val.getValue());
        }
        throw new RuntimeException("该值不支持取负数:" + Arrays.toString(param));
    }
}
