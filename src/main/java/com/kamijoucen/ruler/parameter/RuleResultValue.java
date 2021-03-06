package com.kamijoucen.ruler.parameter;

public class RuleResultValue {

    public final Object value;

    public RuleResultValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Boolean toBoolean() {
        return (Boolean) value;
    }

    public Integer toInteger() {
        return (Integer) value;
    }

    public Double toDouble() {
        return (Double) value;
    }

}
