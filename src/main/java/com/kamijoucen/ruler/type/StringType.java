package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class StringType implements BaseValue {

    public static final StringType INSTANCE = new StringType();

    private StringType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }
}
