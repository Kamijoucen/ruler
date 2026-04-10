package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class BoolType implements BaseValue {

    public static final BoolType INSTANCE = new BoolType();

    private BoolType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOL;
    }

}
