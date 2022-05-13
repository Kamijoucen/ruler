package com.kamijoucen.ruler.parameter;

public class RuleResultValue {

    public final Object value;

    public RuleResultValue(Object value) {
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
