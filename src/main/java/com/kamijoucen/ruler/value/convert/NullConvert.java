package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

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
