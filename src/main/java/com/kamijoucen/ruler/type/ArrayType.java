package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class ArrayType implements BaseValue {

    public static final ArrayType INSTANCE = new ArrayType();

    private ArrayType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }

}
