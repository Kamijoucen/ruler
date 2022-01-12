package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RClassInfo;

public class RsonValue extends AbstractRClassValue {

    public RsonValue(RClassInfo mateData) {
        super(mateData);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
