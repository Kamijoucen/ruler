package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class UnknownType implements BaseValue {

    public static final UnknownType INSTANCE = new UnknownType();

    private UnknownType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.UN_KNOWN;
    }

}
