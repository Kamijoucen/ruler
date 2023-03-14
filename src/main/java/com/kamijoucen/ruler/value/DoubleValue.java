package com.kamijoucen.ruler.value;

public class DoubleValue extends AbstractValue {

    private double value;

    public DoubleValue(double value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
