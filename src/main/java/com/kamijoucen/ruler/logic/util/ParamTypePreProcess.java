package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.parameter.RulerParameter;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ParamTypePreProcess {

    private ParamTypePreProcess() {
    }

    public static List<RulerParameter> process(RulerConfiguration configuration, Map<String, Object> param) {
        if (CollectionUtil.isEmpty(param)) {
            return Collections.emptyList();
        }
        List<RulerParameter> list = new ArrayList<>(param.size());
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            list.add(processOne(configuration, entry));
        }
        return list;
    }

    private static RulerParameter processOne(RulerConfiguration configuration, Map.Entry<String, Object> entry) {
        Object value = entry.getValue();
        if (value == null) {
            return new RulerParameter(ValueType.NULL, entry.getKey(), null);
        }
        if (value.getClass().isArray()) {
            return new RulerParameter(ValueType.ARRAY, entry.getKey(), value);
        }
        if (value instanceof List) {
            return new RulerParameter(ValueType.ARRAY, entry.getKey(), value);
        }

        ValueConvert convert = configuration.getValueConvertManager().getConverter(value);
        if (convert == null) {
            throw new IllegalArgumentException("unsupported parameter type: " + value.getClass());
        }
        return new RulerParameter(convert.getType(), entry.getKey(), value);
    }
}
