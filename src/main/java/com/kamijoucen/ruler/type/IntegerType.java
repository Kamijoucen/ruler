package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class IntegerType implements BaseValue {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {
    }

    @Override
    public ValueType getType() {
        return null;
    }

}
