package com.kamijoucen.ruler.logic.convert;

import com.kamijoucen.ruler.types.value.ValueConvert;
import com.kamijoucen.ruler.types.value.ValueType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class ValueConversions {
    private static final Map<ValueType, ValueConvert> VALUE_TYPE_MAP;
    private static final Map<Class<?>, ValueConvert> CLASS_MAP;
    private ValueConversions() {}
    static {
        Map<ValueType, ValueConvert> types = new HashMap<>();
        Map<Class<?>, ValueConvert> classes = new HashMap<>();

        IntegerConvert integerConvert = new IntegerConvert();
        DoubleConvert doubleConvert = new DoubleConvert();
        StringConvert stringConvert = new StringConvert();
        NullConvert nullConvert = new NullConvert();
        ArrayConvert arrayConvert = new ArrayConvert();
        BoolConvert boolConvert = new BoolConvert();
        SubRuleResultConvert subRuleResultConvert = new SubRuleResultConvert();
        DateConvert dateConvert = new DateConvert();
        MapRsonConvert mapRsonConvert = new MapRsonConvert();

        types.put(ValueType.INTEGER, integerConvert);
        types.put(ValueType.DOUBLE, doubleConvert);
        types.put(ValueType.STRING, stringConvert);
        types.put(ValueType.NULL, nullConvert);
        types.put(ValueType.ARRAY, arrayConvert);
        types.put(ValueType.BOOL, boolConvert);
        types.put(ValueType.DATE, dateConvert);
        types.put(ValueType.RSON, mapRsonConvert);
        types.put(ValueType.RULE_RESULT, subRuleResultConvert);

        classes.put(Integer.class, integerConvert);
        classes.put(Long.class, integerConvert);
        classes.put(BigInteger.class, integerConvert);
        classes.put(Double.class, doubleConvert);
        classes.put(Float.class, doubleConvert);
        classes.put(BigDecimal.class, doubleConvert);
        classes.put(String.class, stringConvert);
        classes.put(Boolean.class, boolConvert);
        classes.put(Date.class, dateConvert);


        VALUE_TYPE_MAP = Map.copyOf(types);
        CLASS_MAP = Map.copyOf(classes);
    }
    public static ValueConvert getConverter(Object obj) {
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

    public static ValueConvert getConverter(ValueType type) {
        return VALUE_TYPE_MAP.get(type);
    }
}
