package com.kamijoucen.ruler.domain.value.convert;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class NullConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        return NullValue.INSTANCE;
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        return null;
    }
}
