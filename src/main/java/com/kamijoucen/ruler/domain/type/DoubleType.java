package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class DoubleType implements BaseValue {

    public static final DoubleType INSTANCE = new DoubleType();

    private DoubleType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

}
