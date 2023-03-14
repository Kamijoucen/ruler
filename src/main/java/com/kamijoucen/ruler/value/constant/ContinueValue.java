package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.AbstractValue;
import com.kamijoucen.ruler.value.ValueType;

public class ContinueValue extends AbstractValue {

    public static final ContinueValue INSTANCE = new ContinueValue();

    @Override
    public ValueType getType() {
        return ValueType.CONTINUE;
    }


}
