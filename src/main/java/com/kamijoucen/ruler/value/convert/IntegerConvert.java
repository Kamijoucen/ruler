package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

public class IntegerConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public BaseValue realToBase(Object value) {
        return new IntegerValue(Long.parseLong(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return Long.parseLong(value.toString());
    }

}
