package com.kamijoucen.ruler.parameter;

import com.kamijoucen.ruler.value.ValueType;

public class RulerParameter {

    private ValueType type;
    private String name;
    private Object value;

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
