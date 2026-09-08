package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.StringValue;
import com.kamijoucen.ruler.types.value.ValueType;

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
