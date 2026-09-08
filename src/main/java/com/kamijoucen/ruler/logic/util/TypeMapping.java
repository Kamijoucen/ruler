package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.types.value.ValueType;

import java.util.Map;

public final class TypeMapping {

    private TypeMapping() {
    }

    private static final Map<ValueType, String> mapping = Map.of(
            ValueType.FUNCTION, "function", ValueType.CLOSURE, "function",
            ValueType.INTEGER, "int", ValueType.DOUBLE, "double",
            ValueType.STRING, "string", ValueType.BOOL, "boolean",
            ValueType.ARRAY, "array", ValueType.NULL, "null", ValueType.RSON, "object");

    public static String find(ValueType type) {
        return mapping.get(type);
    }

}
