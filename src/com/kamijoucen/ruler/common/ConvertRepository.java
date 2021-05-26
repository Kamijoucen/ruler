package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.convert.*;

import java.util.HashMap;
import java.util.Map;

public class ConvertRepository {

    private static final Map<ValueType, ValueConvert> VALUE_TYPE_MAP = new HashMap<ValueType, ValueConvert>();

    private static final Map<Class<?>, ValueConvert> CLASS_MAP = new HashMap<Class<?>, ValueConvert>();

    static {

        IntegerConvert integerConvert = new IntegerConvert();
        DoubleConvert doubleConvert = new DoubleConvert();
        StringConvert stringConvert = new StringConvert();
        NullConvert nullConvert = new NullConvert();

        VALUE_TYPE_MAP.put(ValueType.INTEGER, integerConvert);
        VALUE_TYPE_MAP.put(ValueType.DOUBLE, doubleConvert);
        VALUE_TYPE_MAP.put(ValueType.STRING, stringConvert);
        VALUE_TYPE_MAP.put(ValueType.NULL, nullConvert);

        CLASS_MAP.put(Integer.class, integerConvert);
        CLASS_MAP.put(Double.class, doubleConvert);
        CLASS_MAP.put(String.class, stringConvert);
    }

    public static ValueConvert getConverter(Object obj) {
        if (obj == null) {
            return VALUE_TYPE_MAP.get(ValueType.NULL);
        }
        return CLASS_MAP.get(obj.getClass());
    }

    public static ValueConvert getConverter(ValueType type) {
        return VALUE_TYPE_MAP.get(type);
    }
}