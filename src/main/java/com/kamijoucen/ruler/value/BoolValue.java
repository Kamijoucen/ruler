package com.kamijoucen.ruler.value;

public class BoolValue implements BaseValue {

    public static final BoolValue TRUE = new BoolValue(true);
    public static final BoolValue FALSE = new BoolValue(false);
    
    private final boolean value;

    private BoolValue(boolean value) {
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

    public static BoolValue get(boolean bool) {
        return bool ? TRUE : FALSE;
    }
    
}
