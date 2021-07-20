package com.kamijoucen.ruler.value.constant;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class ContinueValue implements BaseValue {

    public static final ContinueValue INSTANCE = new ContinueValue();

    @Override
    public ValueType getType() {
        return ValueType.CONTINUE;
    }


}
