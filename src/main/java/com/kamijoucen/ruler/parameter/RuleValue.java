package com.kamijoucen.ruler.parameter;

public class RuleValue {

    public final Object value;

    public RuleValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getString() {
        return value.toString();
    }

    public Boolean getBoolean() {
        return (Boolean) value;
    }

    public Integer getInteger() {
        return (Integer) value;
    }

    public Double getDouble() {
        return (Double) value;
    }
}
