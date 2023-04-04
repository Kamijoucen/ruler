package com.kamijoucen.ruler.value;

import java.util.HashMap;
import java.util.Map;

public class RsonValue extends AbstractValue implements Rson {

    private final Map<String, BaseValue> fields = new HashMap<>();

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

    @Override
    public BaseValue getField(String name) {
        return fields.get(name);
    }

    @Override
    public void putField(String name, BaseValue baseValue) {
        fields.put(name, baseValue);
    }

    @Override
    public Map<String, BaseValue> getFields() {
        return fields;
    }

}
