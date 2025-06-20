package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ArrayConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        List<Object> realArr;
        if (value instanceof Collection) {
            realArr = new ArrayList<>((Collection<?>) value);
        } else {
            realArr = Arrays.asList((Object[]) value);
        }
        List<BaseValue> list = new ArrayList<>(realArr.size());
        for (Object obj : realArr) {
            ValueConvert convert = configuration.getValueConvertManager().getConverter(obj);
            BaseValue baseValue = convert.realToBase(obj, configuration);
            list.add(baseValue);
        }
        return new ArrayValue(list);
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        ArrayValue arrayValue = (ArrayValue) value;
        List<BaseValue> values = arrayValue.getValues();
        List<Object> objs = new ArrayList<>(values.size());
        for (BaseValue val : values) {
            ValueConvert convert = configuration.getValueConvertManager().getConverter(val.getType());
            Object obj = convert.baseToReal(val, configuration);
            objs.add(obj);
        }
        return objs;
    }
}
