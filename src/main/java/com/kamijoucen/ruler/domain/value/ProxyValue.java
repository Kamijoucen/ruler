package com.kamijoucen.ruler.domain.value;

public class ProxyValue extends AbstractValue {

    private final BaseValue target;

    private final RsonValue handler;

    public ProxyValue(BaseValue target, RsonValue handler) {
        this.target = target;
        this.handler = handler;
    }

    @Override
    public ValueType getType() {
        return ValueType.PROXY;
    }

    public BaseValue getTarget() {
        return target;
    }

    public RsonValue getHandler() {
        return handler;
    }

}
