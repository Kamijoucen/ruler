package com.kamijoucen.ruler.value;

public class RsonValue extends AbstractValue {

    public RsonValue(RClassValue classValue) {
        super(classValue);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
