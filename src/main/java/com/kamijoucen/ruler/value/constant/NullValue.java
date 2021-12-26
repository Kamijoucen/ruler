package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class NullValue implements BaseValue {

    public static final NullValue INSTANCE = new NullValue();

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    @Override
    public String toString() {
        return "null";
    }

}
