package com.kamijoucen.ruler.domain.value.convert;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class BoolConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.BOOL;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        return BoolValue.get((Boolean) value);
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        BoolValue boolValue = (BoolValue) value;
        return boolValue.getValue();
    }
}
