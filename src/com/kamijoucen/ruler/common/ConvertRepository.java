package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.convert.DoubleConvert;
import com.kamijoucen.ruler.value.convert.IntegerConvert;
import com.kamijoucen.ruler.value.convert.StringConvert;
import com.kamijoucen.ruler.value.convert.ValueConvert;

import java.util.HashMap;
import java.util.Map;

public class ConvertRepository {

    private static final Map<ValueType, ValueConvert> CONVERT_MAP = new HashMap<ValueType, ValueConvert>();

    static {
        CONVERT_MAP.put(ValueType.INTEGER, new IntegerConvert());
        CONVERT_MAP.put(ValueType.DOUBLE, new DoubleConvert());
        CONVERT_MAP.put(ValueType.STRING, new StringConvert());


    }


    public static ValueConvert getConverter(ValueType type) {
        return CONVERT_MAP.get(type);
    }
}
