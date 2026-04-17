package com.kamijoucen.ruler.domain.value.convert;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

import java.math.BigInteger;

public class IntegerConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        if (value instanceof BigInteger) {
            return configuration.getIntegerNumberCache().getValue((BigInteger) value);
        }
        return configuration.getIntegerNumberCache().getValue(new BigInteger(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        return new BigInteger(value.toString());
    }

}
