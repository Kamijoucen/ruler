package com.kamijoucen.ruler.logic.convert;
import com.kamijoucen.ruler.types.value.ValueConvert;

import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.RsonValue;
import com.kamijoucen.ruler.types.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class MapRsonConvert implements ValueConvert {

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

    @Override
    public BaseValue realToBase(Object value) {
        Map<?, ?> map = (Map<?, ?>) value;

        RsonValue rsonValue = new RsonValue();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            ValueConvert convert = ValueConversions.getConverter(entry.getValue());
            BaseValue baseValue = convert.realToBase(entry.getValue());
            rsonValue.getFields().put(entry.getKey().toString(), baseValue);
        }
        return rsonValue;
    }

    @Override
    public Object baseToReal(BaseValue value) {
        RsonValue rsonValue = (RsonValue) value;
        Map<String, Object> map = new HashMap<>(
                (int) (Math.ceil(rsonValue.getFields().size() / 0.75) + 1));
        for (Map.Entry<String, BaseValue> entry : rsonValue.getFields().entrySet()) {
            ValueConvert convert = ValueConversions.getConverter(entry.getValue().getType());
            map.put(entry.getKey(), convert.baseToReal(entry.getValue()));
        }
        return map;
    }
}
