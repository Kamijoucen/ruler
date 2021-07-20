package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class StringConvert implements ValueConvert {
    @Override
    public BaseValue realToBase(Object value) {
        return new StringValue((String) value);
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return value.toString();
    }
}
