package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class OrOperation implements Operation {
    @Override
    public BaseValue compute(BaseValue... param) {
        BaseValue tempVal1 = param[0];
        BaseValue tempVal2 = param[1];

        if (tempVal1.getType() != ValueType.BOOL
                || tempVal2.getType() != ValueType.BOOL) {

            throw SyntaxException.withSyntax("仅bool类型支持&&运算");
        }

        BoolValue lVal = (BoolValue) tempVal1;
        BoolValue rVal = (BoolValue) tempVal2;

        return new BoolValue(lVal.getValue() && rVal.getValue());
    }
}
