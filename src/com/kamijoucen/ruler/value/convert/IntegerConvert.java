package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;

public class IntegerConvert implements ValueConvert {
    @Override
    public BaseValue realToBase(Object value) {
        return null;
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return Integer.parseInt(value.toString());
    }

}
