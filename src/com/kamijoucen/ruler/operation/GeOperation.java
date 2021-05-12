package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;

public class GeOperation implements Operation {

    @Override
    public BaseValue eval(BaseValue... param) {
        BaseValue tempVal1 = param[0];
        BaseValue tempVal2 = param[1];

        if (tempVal1.getType() == ValueType.INTEGER && tempVal2.getType() == ValueType.INTEGER) {

            IntegerValue val1 = (IntegerValue) tempVal1;
            IntegerValue val2 = (IntegerValue) tempVal2;

            return new BoolValue(val1.getValue() >= val2.getValue());
        } else if (tempVal1.getType() == ValueType.INTEGER && tempVal2.getType() == ValueType.DOUBLE) {

            IntegerValue val1 = (IntegerValue) tempVal1;
            DoubleValue val2 = (DoubleValue) tempVal2;

            return new BoolValue(val1.getValue() >= val2.getValue());
        } else if (tempVal1.getType() == ValueType.DOUBLE && tempVal2.getType() == ValueType.INTEGER) {

            DoubleValue val1 = (DoubleValue) tempVal1;
            IntegerValue val2 = (IntegerValue) tempVal2;

            return new BoolValue(val1.getValue() >= val2.getValue());
        } else if (tempVal1.getType() == ValueType.DOUBLE && tempVal2.getType() == ValueType.DOUBLE) {

            DoubleValue val1 = (DoubleValue) tempVal1;
            DoubleValue val2 = (DoubleValue) tempVal2;

            return new BoolValue(val1.getValue() >= val2.getValue());
        }

        throw SyntaxException.withSyntax("该值不支持 '>=':" + Arrays.toString(param));
    }
}
