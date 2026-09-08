package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.IntegerValue;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ValueType;

import java.math.BigInteger;

public class IntegerConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public BaseValue realToBase(Object value) {
        if (value instanceof BigInteger) {
            return IntegerValue.valueOf((BigInteger) value);
        }
        return IntegerValue.valueOf(new BigInteger(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return new BigInteger(value.toString());
    }

}
