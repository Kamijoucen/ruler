package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RMetaInfo;

public class RsonValue extends AbstractRClassValue {

    public RsonValue(RMetaInfo metaInfo) {
        super(metaInfo);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
