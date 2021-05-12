package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class VoidValue implements BaseValue {

    public static final VoidValue INSTANCE = new VoidValue();

    @Override
    public ValueType getType() {
        return ValueType.VOID;
    }


}
