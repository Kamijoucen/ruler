package com.kamijoucen.ruler.value;

public abstract class AbstractValue implements BaseValue {
    private final RClassValue classInfo;

    public AbstractValue(RClassValue classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public RClassValue getRClass() {
        return classInfo;
    }
}
