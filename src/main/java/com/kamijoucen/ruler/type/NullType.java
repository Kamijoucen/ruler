package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class NullType implements BaseValue {

    public static final NullType INSTANCE = new NullType();

    private NullType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

}
