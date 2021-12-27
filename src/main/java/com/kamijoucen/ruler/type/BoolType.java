package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class BoolType implements BaseValue {

    public static final BoolType INSTANCE = new BoolType();

    private BoolType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOL;
    }

}
