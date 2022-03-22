package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

public class StringConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public BaseValue realToBase(Object value) {
        return new StringValue((String) value);
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return value.toString();
    }
}
