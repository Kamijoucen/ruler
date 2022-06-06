package com.kamijoucen.ruler.value.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.common.RMateInfo;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class ArrayConvert implements ValueConvert {
    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        List<Object> realArr = null;
        if (value instanceof Collection) {
            realArr = new ArrayList<Object>((Collection<?>) value);
        } else {
            realArr = Arrays.asList((Object[]) value);
        }
        List<BaseValue> list = new ArrayList<BaseValue>(realArr.size());
        for (Object obj : realArr) {
            BaseValue baseValue = ConvertRepository.getConverter(obj).realToBase(obj, configuration);
            list.add(baseValue);
        }
        return new ArrayValue(list, new RMateInfo());
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        ArrayValue arrayValue = (ArrayValue) value;
        List<BaseValue> values = arrayValue.getValues();
        List<Object> objs = new ArrayList<Object>(values.size());
        for (BaseValue val : values) {
            Object obj = ConvertRepository.getConverter(val.getType()).baseToReal(val, configuration);
            objs.add(obj);
        }
        return objs;
    }
}
