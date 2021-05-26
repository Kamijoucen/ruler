package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;

public class DoubleConvert implements ValueConvert {
    @Override
    public BaseValue realToBase(Object value) {
        return new DoubleValue(Double.parseDouble(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return Double.parseDouble(value.toString());
    }
}
