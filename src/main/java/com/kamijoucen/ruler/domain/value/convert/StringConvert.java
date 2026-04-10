package com.kamijoucen.ruler.domain.value.convert;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class StringConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        return new StringValue((String) value);
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        return value.toString();
    }
}
