package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DateValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Date;

public class DateConvert implements ValueConvert {

    @Override
    public ValueType getType() {
        return ValueType.DATE;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        return new DateValue((Date) value);
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        DateValue dateValue = (DateValue) value;
        return dateValue.getValue();
    }
}
