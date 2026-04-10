package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class StringType implements BaseValue {

    public static final StringType INSTANCE = new StringType();

    private StringType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }
}
