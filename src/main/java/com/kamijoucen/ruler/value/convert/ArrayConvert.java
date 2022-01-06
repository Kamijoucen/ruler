package com.kamijoucen.ruler.value.convert;

import java.util.ArrayList;
import java.util.List;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.runtime.RClassInfo;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;

public class ArrayConvert implements ValueConvert {
    @Override
    public BaseValue realToBase(Object value) {
        Object[] arr = (Object[]) value;
        List<BaseValue> list = new ArrayList<BaseValue>(arr.length);
        for (Object obj : arr) {
            BaseValue baseValue = ConvertRepository.getConverter(obj).realToBase(obj);
            list.add(baseValue);
        }
        return new ArrayValue(list, new RClassInfo());
    }

    @Override
    public Object baseToReal(BaseValue value) {
        ArrayValue arrayValue = (ArrayValue) value;
        List<BaseValue> values = arrayValue.getValues();
        List<Object> objs = new ArrayList<Object>(values.size());
        for (BaseValue val : values) {
            Object obj = ConvertRepository.getConverter(val.getType()).baseToReal(val);
            objs.add(obj);
        }
        return objs;
    }
}
