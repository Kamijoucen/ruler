package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class NoneValue implements BaseValue {

    public static final NoneValue INSTANCE = new NoneValue();

    @Override
    public ValueType getType() {
        return ValueType.NONE;
    }
}
