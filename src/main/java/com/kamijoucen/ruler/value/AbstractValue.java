package com.kamijoucen.ruler.value;

public abstract class AbstractValue implements BaseValue {
    private final IRClassValue classInfo;

    public AbstractValue(IRClassValue classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public IRClassValue getRClass() {
        return classInfo;
    }
}
