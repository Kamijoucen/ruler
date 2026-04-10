package com.kamijoucen.ruler.domain.type;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class FailureType implements BaseValue {

    public static final FailureType INSTANCE = new FailureType();

    private FailureType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.FAILURE;
    }

}
