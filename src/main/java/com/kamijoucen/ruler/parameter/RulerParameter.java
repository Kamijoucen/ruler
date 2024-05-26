package com.kamijoucen.ruler.parameter;

import com.kamijoucen.ruler.value.ValueType;

public class RulerParameter {

    private final ValueType type;
    private final String name;
    private final Object value;

    public RulerParameter(ValueType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
