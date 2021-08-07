package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvertUtil {

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
