package com.kamijoucen.ruler.value;

public class RsonValue extends AbstractValue {

    public RsonValue(IRClassValue classValue) {
        super(classValue);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

}
