package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class TypeMapping {

    private static final Map<ValueType, String> mapping = new HashMap<>();

    static {
        mapping.put(ValueType.FUNCTION, "function");
        mapping.put(ValueType.CLOSURE, "function");
        mapping.put(ValueType.INTEGER, "int");
        mapping.put(ValueType.DOUBLE, "double");
        mapping.put(ValueType.STRING, "string");
        mapping.put(ValueType.BOOL, "boolean");
        mapping.put(ValueType.ARRAY, "array");
        mapping.put(ValueType.NULL, "null");
        mapping.put(ValueType.RSON, "object");
    }

    public static String find(ValueType type) {
        return mapping.get(type);
    }

}
