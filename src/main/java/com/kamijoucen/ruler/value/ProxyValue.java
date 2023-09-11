package com.kamijoucen.ruler.value;

import java.util.Map;

public class ProxyValue extends RsonValue {

    private RsonValue value;

    private ClosureValue getCallback;

    private ClosureValue putCallback;

    public ProxyValue(RsonValue value, ClosureValue getCallback, ClosureValue putCallback) {
        this.value = value;
        this.getCallback = getCallback;
        this.putCallback = putCallback;
    }

    @Override
    public BaseValue getField(String name) {
        // TODO Auto-generated method stub
        return super.getField(name);
    }

    @Override
    public Map<String, BaseValue> getFields() {
        // TODO Auto-generated method stub
        return super.getFields();
    }

    @Override
    public void putField(String name, BaseValue baseValue) {
        // TODO Auto-generated method stub
        super.putField(name, baseValue);
    }

    public RsonValue getValue() {
        return value;
    }

    public ClosureValue getGetCallback() {
        return getCallback;
    }

    public ClosureValue getPutCallback() {
        return putCallback;
    }

}
