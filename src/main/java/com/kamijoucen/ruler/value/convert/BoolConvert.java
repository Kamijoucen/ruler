package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

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
