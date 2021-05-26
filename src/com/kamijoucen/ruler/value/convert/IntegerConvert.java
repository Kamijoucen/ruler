package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;

public class IntegerConvert implements ValueConvert {
    @Override
    public BaseValue realToBase(Object value) {
        return new IntegerValue(Integer.parseInt(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return Integer.parseInt(value.toString());
    }

}