package com.kamijoucen.ruler.value;

public abstract class AbstractValue implements BaseValue {

    private final RClass classInfo;

    public AbstractValue(RClass classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public RClass getRClass() {
        return classInfo;
    }
}
