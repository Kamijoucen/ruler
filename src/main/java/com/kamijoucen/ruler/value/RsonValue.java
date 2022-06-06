package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RMateInfo;

public class RsonValue extends AbstractRClassValue {

    public RsonValue(RMateInfo mateData) {
        super(mateData);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
