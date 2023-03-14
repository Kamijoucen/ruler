package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.AbstractValue;
import com.kamijoucen.ruler.value.ValueType;

public class ReturnValue extends AbstractValue {

    public static final ReturnValue INSTANCE = new ReturnValue();

    @Override
    public ValueType getType() {
        return ValueType.RETURN;
    }


}
