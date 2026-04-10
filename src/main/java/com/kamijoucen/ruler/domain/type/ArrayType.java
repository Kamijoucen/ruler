package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class ArrayType implements BaseValue {

    public static final ArrayType INSTANCE = new ArrayType();

    private ArrayType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }

}
