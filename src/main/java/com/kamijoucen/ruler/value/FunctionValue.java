package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.function.RulerFunction;

public class FunctionValue extends AbstractValue {

    private RulerFunction value;

    public FunctionValue(RulerFunction value, RClass rClass) {
        super(rClass);
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    public RulerFunction getValue() {
        return value;
    }

    public void setValue(RulerFunction value) {
        this.value = value;
    }
}
