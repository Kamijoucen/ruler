package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class FailureType implements BaseValue {

    public static final FailureType INSTANCE = new FailureType();

    private FailureType() {
    }

    @Override
    public ValueType getType() {
        return null;
    }

}
