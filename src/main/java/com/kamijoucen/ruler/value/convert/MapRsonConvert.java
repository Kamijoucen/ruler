package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class MapRsonConvert implements ValueConvert {

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

    @Override
    public BaseValue realToBase(Object value, RulerConfiguration configuration) {
        Map<?, ?> map = (Map<?, ?>) value;

        RsonValue rsonValue = new RsonValue();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            ValueConvert convert = configuration.getValueConvertManager().getConverter(entry.getValue());
            BaseValue baseValue = convert.realToBase(entry.getValue(), configuration);
            rsonValue.getFields().put(entry.getKey().toString(), baseValue);
        }
        return rsonValue;
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        RsonValue rsonValue = (RsonValue) value;
        Map<String, Object> map = new HashMap<String, Object>(
                (int) (Math.ceil(rsonValue.getFields().size() / 0.75) + 1));
        for (Map.Entry<String, BaseValue> entry : rsonValue.getFields().entrySet()) {
            ValueConvert convert = configuration.getValueConvertManager().getConverter(entry.getValue().getType());
            map.put(entry.getKey(), convert.baseToReal(entry.getValue(), configuration));
        }

        return map;
    }
}
