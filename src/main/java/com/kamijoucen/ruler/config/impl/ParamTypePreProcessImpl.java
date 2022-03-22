package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.config.ParamTypePreProcess;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.convert.ValueConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParamTypePreProcessImpl implements ParamTypePreProcess {

    @Override
    public List<RulerParameter> process(Map<String, Object> param) {
        if (CollectionUtil.isEmpty(param)) {
            return Collections.emptyList();
        }
        List<RulerParameter> list = new ArrayList<RulerParameter>(param.size());
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            list.add(processOne(entry));
        }
        return list;
    }

    private RulerParameter processOne(Map.Entry<String, Object> entry) {
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
        ValueConvert converter = ConvertRepository.getConverter(value);
        if (converter == null) {
            throw new IllegalArgumentException("不支持的参数：" + value.getClass());
        }
        return new RulerParameter(converter.getType(), entry.getKey(), value);
    }
}
