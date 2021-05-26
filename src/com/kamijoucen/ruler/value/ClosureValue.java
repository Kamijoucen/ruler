package com.kamijoucen.ruler.value;

public class ClosureValue implements BaseValue {

    @Override
    public ValueType getType() {
        return ValueType.CLOSURE;
    }
}
