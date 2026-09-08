package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.ValueType;

public class BoolConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.BOOL;
    }

    @Override
    public BaseValue realToBase(Object value) {
        return BoolValue.get((Boolean) value);
    }

    @Override
    public Object baseToReal(BaseValue value) {
        BoolValue boolValue = (BoolValue) value;
        return boolValue.getValue();
    }
}
