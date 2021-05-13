package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class BreakValue implements BaseValue {

    public static final BreakValue INSTANCE = new BreakValue();

    @Override
    public ValueType getType() {
        return ValueType.BREAK;
    }


}
