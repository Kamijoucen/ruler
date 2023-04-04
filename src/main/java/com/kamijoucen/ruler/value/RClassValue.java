package com.kamijoucen.ruler.value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RClassValue extends AbstractValue implements RClass {

    private final Map<String, BaseValue> properties = new ConcurrentHashMap<String, BaseValue>();

    @Override
    public Map<String, BaseValue> getProperties() {
        return properties;
    }

    @Override
    public BaseValue getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public ValueType getType() {
        return ValueType.RCLASS;
    }
}
