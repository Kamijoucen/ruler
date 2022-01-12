package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class UnknownType implements BaseValue {

    public static final UnknownType INSTANCE = new UnknownType();

    private UnknownType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.UN_KNOWN;
    }

}
