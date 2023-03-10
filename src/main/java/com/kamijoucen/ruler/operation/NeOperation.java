package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.*;

public class NeOperation implements Operation {

    @Override
    public BaseValue compute(RuntimeContext context, BaseValue... param) {
        BaseValue tempVal1 = param[0];
        BaseValue tempVal2 = param[1];
        if (tempVal1.getType() == ValueType.INTEGER && tempVal2.getType() == ValueType.INTEGER) {
            IntegerValue val1 = (IntegerValue) tempVal1;
            IntegerValue val2 = (IntegerValue) tempVal2;
            return BoolValue.get(val1.getValue() != val2.getValue());
        } else if (tempVal1.getType() == ValueType.INTEGER && tempVal2.getType() == ValueType.DOUBLE) {
            IntegerValue val1 = (IntegerValue) tempVal1;
            DoubleValue val2 = (DoubleValue) tempVal2;
            return BoolValue.get(val1.getValue() != val2.getValue());
        } else if (tempVal1.getType() == ValueType.DOUBLE && tempVal2.getType() == ValueType.INTEGER) {
            DoubleValue val1 = (DoubleValue) tempVal1;
            IntegerValue val2 = (IntegerValue) tempVal2;
            return BoolValue.get(val1.getValue() != val2.getValue());
        } else if (tempVal1.getType() == ValueType.DOUBLE && tempVal2.getType() == ValueType.DOUBLE) {
            DoubleValue val1 = (DoubleValue) tempVal1;
            DoubleValue val2 = (DoubleValue) tempVal2;
            return BoolValue.get(val1.getValue() != val2.getValue());
        } else {
            if (tempVal1.getType() == ValueType.NULL && tempVal2.getType() == ValueType.NULL) {
                return BoolValue.get(false);
            }
            if (tempVal1.getType() == ValueType.NULL || tempVal2.getType() == ValueType.NULL) {
                return BoolValue.get(true);
            }
            if (tempVal1.getType() == ValueType.STRING || tempVal2.getType() == ValueType.STRING) {
                String strVal1 = tempVal1.toString();
                String strVal2 = tempVal2.toString();
                return BoolValue.get(!strVal1.equals(strVal2));
            }

            if (tempVal1.getType() != tempVal2.getType()) {
                return BoolValue.get(true);
            }
            String strVal1 = tempVal1.toString();
            String strVal2 = tempVal2.toString();
            return BoolValue.get(!strVal1.equals(strVal2));
        }
    }
}
