package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class RsonType implements BaseValue {

    public static final RsonType INSTANCE = new RsonType();

    private RsonType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
