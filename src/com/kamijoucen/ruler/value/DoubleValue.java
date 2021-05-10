package com.kamijoucen.ruler.value;

public class DoubleValue implements BaseValue {

    private double value;

    public DoubleValue(double value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
