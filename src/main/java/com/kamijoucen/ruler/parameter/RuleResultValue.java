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

    public boolean toBoolean() {
        return (Boolean) value;
    }

    public long toInteger() {
        return (long) value;
    }

    public double toDouble() {
        return (Double) value;
    }

}
