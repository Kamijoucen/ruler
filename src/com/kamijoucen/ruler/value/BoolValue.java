package com.kamijoucen.ruler.value;

public class BoolValue implements BaseValue {

    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOL;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
