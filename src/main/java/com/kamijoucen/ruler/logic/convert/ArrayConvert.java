package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ValueType;

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
    public BaseValue realToBase(Object value) {
        List<Object> realArr;
        if (value instanceof Collection) {
            realArr = new ArrayList<Object>((Collection<?>) value);
        } else {
            realArr = Arrays.asList((Object[]) value);
        }
        List<BaseValue> list = new ArrayList<BaseValue>(realArr.size());
        for (Object obj : realArr) {
            ValueConvert convert = ValueConversions.getConverter(obj);
            BaseValue baseValue = convert.realToBase(obj);
            list.add(baseValue);
        }
        return new ArrayValue(list);
    }

    @Override
    public Object baseToReal(BaseValue value) {
        ArrayValue arrayValue = (ArrayValue) value;
        List<BaseValue> values = arrayValue.getValues();
        List<Object> objs = new ArrayList<Object>(values.size());
        for (BaseValue val : values) {
            ValueConvert convert = ValueConversions.getConverter(val.getType());
            Object obj = convert.baseToReal(val);
            objs.add(obj);
        }
        return objs;
    }
}
