package com.kamijoucen.ruler.value;

public class RsonValue extends AbstractMataValue {

    public RsonValue() {
        super(null);
        //TODO Auto-generated constructor stub
    }

    
    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }
    
}
