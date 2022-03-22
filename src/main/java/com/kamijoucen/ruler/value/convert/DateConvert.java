package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class DateConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.DATE;
    }

    @Override
    public BaseValue realToBase(Object value) {
        return null;
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return null;
    }
}
