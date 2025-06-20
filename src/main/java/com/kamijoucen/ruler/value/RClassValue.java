package com.kamijoucen.ruler.value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RClassValue extends AbstractValue implements RClass {

    private final Map<String, BaseValue> properties = new ConcurrentHashMap<>();

    @Override
    public Map<String, BaseValue> getProperties() {
        return properties;
    }

    @Override
    public BaseValue getProperty(String name) {
        return this.properties.get(name);
    }

    @Override
    public void putProperty(String name, BaseValue value) {
        this.properties.put(name, value);
    }

    @Override
    public ValueType getType() {
        return ValueType.RCLASS;
    }
}
