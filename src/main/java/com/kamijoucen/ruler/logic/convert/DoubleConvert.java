package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.DoubleValue;
import com.kamijoucen.ruler.types.value.ValueType;

import java.math.BigDecimal;

public class DoubleConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

    @Override
    public BaseValue realToBase(Object value) {
        if (value instanceof BigDecimal) {
            return new DoubleValue((BigDecimal) value);
        }
        return new DoubleValue(new BigDecimal(value.toString()));
    }

    @Override
    public Object baseToReal(BaseValue value) {
        return new BigDecimal(value.toString());
    }
}
