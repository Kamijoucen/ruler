package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class NullType implements BaseValue {

    public static final NullType INSTANCE = new NullType();

    private NullType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

}
