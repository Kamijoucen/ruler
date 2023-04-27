package com.kamijoucen.ruler.value;

public abstract class AbstractValue implements BaseValue {

    @Override
    public ReferenceType getReferenceType() {
        return ReferenceType.R_VALUE;
    }
}
