package com.kamijoucen.ruler.value;

public class IntegerValue implements BaseValue {

    private long value;

    public IntegerValue(long value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
