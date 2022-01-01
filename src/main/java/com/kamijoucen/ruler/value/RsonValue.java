package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.runtime.RClassInfo;

public class RsonValue extends AbstractRClassValue {

    public RsonValue(RClassInfo mataData) {
        super(mataData);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
