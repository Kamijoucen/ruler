package com.kamijoucen.ruler.domain.value.convert;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class DoubleConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        return new DoubleValue(Double.parseDouble(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        return Double.parseDouble(value.toString());
    }
}
