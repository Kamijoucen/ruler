package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class IntegerConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        return configuration.getIntegerNumberCache().getValue(Long.parseLong(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        return Long.parseLong(value.toString());
    }

}
