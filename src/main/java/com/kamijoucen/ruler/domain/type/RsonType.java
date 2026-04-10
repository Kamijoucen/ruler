package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class RsonType implements BaseValue {

    public static final RsonType INSTANCE = new RsonType();

    private RsonType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
