package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

public class StringConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        return new StringValue((String) value, configuration.getRClassFactory().getClassValue(ValueType.STRING));
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        return value.toString();
    }
}
