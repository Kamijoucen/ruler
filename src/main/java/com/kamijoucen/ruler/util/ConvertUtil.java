package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.*;

public class ConvertUtil {

    public static Map<String, BaseValue> convertToBase(Map<String, Object> realValues) {

        Map<String, BaseValue> baseValues = new HashMap<String, BaseValue>();

        for (Map.Entry<String, Object> entry : realValues.entrySet()) {
            Object realValue = entry.getValue();
            baseValues.put(entry.getKey(), ConvertRepository.getConverter(realValue).realToBase(realValue));
        }

        return baseValues;
    }

    public static List<BaseValue> convertToBase(List<Object> realValues) {
        if (CollectionUtil.isEmpty(realValues)) {
            return Collections.emptyList();
        }

        List<BaseValue> baseValues = new ArrayList<BaseValue>(realValues.size());

        for (Object realValue : realValues) {
            baseValues.add(ConvertRepository.getConverter(realValue.getClass()).realToBase(realValue));
        }

        return baseValues;
    }

    public static List<Object> convertToReal(List<BaseValue> baseValues) {
        if (CollectionUtil.isEmpty(baseValues)) {
            return Collections.emptyList();
        }

        List<Object> realValues = new ArrayList<Object>(baseValues.size());

        for (BaseValue baseValue : baseValues) {
            realValues.add(ConvertRepository.getConverter(baseValue.getType()).baseToReal(baseValue));
        }

        return realValues;
    }

}
