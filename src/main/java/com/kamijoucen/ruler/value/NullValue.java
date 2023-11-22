package com.kamijoucen.ruler.value;

public class NullValue extends AbstractValue {

    public static final NullValue INSTANCE = new NullValue();

    private NullValue() {
    }

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    @Override
    public String toString() {
        return "null";
    }

}
