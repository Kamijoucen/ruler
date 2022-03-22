package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

public class NullConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    @Override
    public BaseValue realToBase(Object value) {
        return NullValue.INSTANCE;
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return null;
    }
}
