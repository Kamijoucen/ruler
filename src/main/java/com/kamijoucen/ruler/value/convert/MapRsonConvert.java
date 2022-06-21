package com.kamijoucen.ruler.value.convert;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.common.RMetaInfo;
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

        RsonValue rsonValue = new RsonValue(new RMetaInfo());
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            BaseValue baseValue = ConvertRepository.getConverter(entry.getValue()).realToBase(entry.getValue(), configuration);
            rsonValue.getClassInfo().put(entry.getKey().toString(), baseValue);
        }

        return rsonValue;
    }

    @Override
    public Object baseToReal(BaseValue value, RulerConfiguration configuration) {
        RsonValue rsonValue = (RsonValue) value;
        Map<String, Object> map = new HashMap<String, Object>(
                (int) (Math.ceil(rsonValue.getClassInfo().getProperties().size() / 0.75) + 1));
        for (Map.Entry<String, BaseValue> entry : rsonValue.getClassInfo().getProperties().entrySet()) {
            map.put(entry.getKey(), ConvertRepository.getConverter(entry.getValue().getType()).baseToReal(entry.getValue(), configuration));
        }

        return map;
    }
}
