package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.util.ConvertUtil;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;

public class GtOperation implements Operation {

    @Override
    public BaseValue compute(RuntimeContext context, BaseValue... param) {
        BaseValue tempVal1 = param[0];
        BaseValue tempVal2 = param[1];
        if (tempVal1.getType() == ValueType.STRING) {
            tempVal1 = ConvertUtil.stringToValue((StringValue) tempVal1);
        }
        if (tempVal2.getType() == ValueType.STRING) {
            tempVal2 = ConvertUtil.stringToValue((StringValue) tempVal2);
        }
        if (tempVal1.getType() == ValueType.INTEGER && tempVal2.getType() == ValueType.INTEGER) {
            IntegerValue val1 = (IntegerValue) tempVal1;
            IntegerValue val2 = (IntegerValue) tempVal2;
            return BoolValue.get(val1.getValue() > val2.getValue());
        } else if (tempVal1.getType() == ValueType.INTEGER && tempVal2.getType() == ValueType.DOUBLE) {
            IntegerValue val1 = (IntegerValue) tempVal1;
            DoubleValue val2 = (DoubleValue) tempVal2;
            return BoolValue.get(val1.getValue() > val2.getValue());
        } else if (tempVal1.getType() == ValueType.DOUBLE && tempVal2.getType() == ValueType.INTEGER) {
            DoubleValue val1 = (DoubleValue) tempVal1;
            IntegerValue val2 = (IntegerValue) tempVal2;
            return BoolValue.get(val1.getValue() > val2.getValue());
        } else if (tempVal1.getType() == ValueType.DOUBLE && tempVal2.getType() == ValueType.DOUBLE) {
            DoubleValue val1 = (DoubleValue) tempVal1;
            DoubleValue val2 = (DoubleValue) tempVal2;
            return BoolValue.get(val1.getValue() > val2.getValue());
        }
        throw SyntaxException.withSyntax("该值不支持 '>':" + Arrays.toString(param));
    }
}
