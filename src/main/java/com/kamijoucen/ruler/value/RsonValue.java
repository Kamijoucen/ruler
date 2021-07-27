package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.runtime.MataData;

public class RsonValue extends AbstractMataValue {

    public RsonValue(MataData mataData) {
        super(mataData);
    }


    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }
    
}
