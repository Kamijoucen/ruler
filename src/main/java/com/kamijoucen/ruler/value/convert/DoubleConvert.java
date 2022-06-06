package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.ValueType;

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
