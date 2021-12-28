package com.kamijoucen.ruler.type;

import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class UnknowType implements BaseValue {

    public static final UnknowType INSTANCE = new UnknowType();

    private UnknowType() {
    }

    @Override
    public ValueType getType() {
        return ValueType.UN_KNOW;
    }

}
