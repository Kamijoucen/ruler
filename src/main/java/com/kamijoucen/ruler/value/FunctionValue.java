package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.function.RulerFunction;

public class FunctionValue implements BaseValue {

    private AbstractRClassValue classInfo;
    private RulerFunction value;

    public FunctionValue(RulerFunction value) {
        this.value = value;
    }

    public FunctionValue(RulerFunction value, AbstractRClassValue classInfo) {
        this.classInfo = classInfo;
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
