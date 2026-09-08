package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.ValueType;

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
