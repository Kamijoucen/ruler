package com.kamijoucen.ruler.domain.value.convert;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DateValue;
import com.kamijoucen.ruler.domain.value.ValueType;

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
