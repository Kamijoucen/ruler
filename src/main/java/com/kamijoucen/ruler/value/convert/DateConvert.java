package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.common.RMateInfo;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
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
        RMateInfo dateMateInfo = configuration.getMetaInfoFactory().createDateMateInfo();
        DateValue dateValue = new DateValue((Date) value, dateMateInfo);
        dateMateInfo.setSource(dateValue);
        return dateValue;
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        return null;
    }
}
