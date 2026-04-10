package com.kamijoucen.ruler.domain.value;

public class MethodValue extends AbstractValue {

    private final BaseValue target;
    private final BaseValue boundSelf;

    public MethodValue(BaseValue target, BaseValue boundSelf) {
        this.target = target;
        this.boundSelf = boundSelf;
    }

    public BaseValue getTarget() {
        return target;
    }

    public BaseValue getBoundSelf() {
        return boundSelf;
    }

    @Override
    public ValueType getType() {
        return ValueType.METHOD;
    }
}
