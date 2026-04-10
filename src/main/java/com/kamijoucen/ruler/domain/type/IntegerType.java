package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class IntegerType implements BaseValue {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

}
