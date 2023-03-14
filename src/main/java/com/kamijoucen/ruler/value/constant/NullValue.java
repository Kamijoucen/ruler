package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.AbstractValue;
import com.kamijoucen.ruler.value.ValueType;

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
