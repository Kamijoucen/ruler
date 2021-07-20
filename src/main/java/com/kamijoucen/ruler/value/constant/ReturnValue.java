package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class ReturnValue implements BaseValue {

    public static final ReturnValue INSTANCE = new ReturnValue();

    @Override
    public ValueType getType() {
        return ValueType.RETURN;
    }


}
