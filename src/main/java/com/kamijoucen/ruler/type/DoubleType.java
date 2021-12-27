package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class DoubleType implements BaseValue {

    public static final DoubleType INSTANCE = new DoubleType();

    private DoubleType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

}
