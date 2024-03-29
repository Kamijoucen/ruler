package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.ValueConvertManager;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.convert.*;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ValueConvertManagerImpl implements ValueConvertManager {

    private final Map<ValueType, ValueConvert> VALUE_TYPE_MAP = new HashMap<>();

    private final Map<Class<?>, ValueConvert> CLASS_MAP = new HashMap<>();

    public ValueConvertManagerImpl() {

        IntegerConvert integerConvert = new IntegerConvert();
        DoubleConvert doubleConvert = new DoubleConvert();
        StringConvert stringConvert = new StringConvert();
        NullConvert nullConvert = new NullConvert();
        ArrayConvert arrayConvert = new ArrayConvert();
        BoolConvert boolConvert = new BoolConvert();
        SubRuleResultConvert subRuleResultConvert = new SubRuleResultConvert();
        DateConvert dateConvert = new DateConvert();
        MapRsonConvert mapRsonConvert = new MapRsonConvert();

        VALUE_TYPE_MAP.put(ValueType.INTEGER, integerConvert);
        VALUE_TYPE_MAP.put(ValueType.DOUBLE, doubleConvert);
        VALUE_TYPE_MAP.put(ValueType.STRING, stringConvert);
        VALUE_TYPE_MAP.put(ValueType.NULL, nullConvert);
        VALUE_TYPE_MAP.put(ValueType.ARRAY, arrayConvert);
        VALUE_TYPE_MAP.put(ValueType.BOOL, boolConvert);
        VALUE_TYPE_MAP.put(ValueType.DATE, dateConvert);
        VALUE_TYPE_MAP.put(ValueType.RSON, mapRsonConvert);
        VALUE_TYPE_MAP.put(ValueType.RULE_RESULT, subRuleResultConvert);

        CLASS_MAP.put(Integer.class, integerConvert);
        CLASS_MAP.put(Long.class, integerConvert);
        CLASS_MAP.put(Double.class, doubleConvert);
        CLASS_MAP.put(Float.class, doubleConvert);
        CLASS_MAP.put(String.class, stringConvert);
        CLASS_MAP.put(Boolean.class, boolConvert);
        CLASS_MAP.put(Date.class, dateConvert);

    }

    @Override
    public ValueConvert getConverter(Object obj) {
        if (obj == null) {
            return VALUE_TYPE_MAP.get(ValueType.NULL);
        }
        if (obj.getClass().isArray() || obj instanceof Collection) {
            return VALUE_TYPE_MAP.get(ValueType.ARRAY);
        }
        if (obj instanceof Map) {
            return VALUE_TYPE_MAP.get(ValueType.RSON);
        }
        return CLASS_MAP.get(obj.getClass());
    }

    @Override
    public ValueConvert getConverter(ValueType type) {
        return VALUE_TYPE_MAP.get(type);
    }
}
