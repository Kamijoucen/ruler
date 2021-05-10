package com.kamijoucen.ruler.value;

public class IntegerValue implements BaseValue {

    private int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
