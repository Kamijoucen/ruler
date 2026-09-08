package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.DateValue;
import com.kamijoucen.ruler.types.value.ValueType;

import java.util.Date;

public class DateConvert implements ValueConvert {

    @Override
    public ValueType getType() {
        return ValueType.DATE;
    }

    @Override
    public BaseValue realToBase(Object value) {
        return new DateValue((Date) value);
    }

    @Override
    public Object baseToReal(BaseValue value) {
        DateValue dateValue = (DateValue) value;
        return dateValue.getValue();
    }
}
