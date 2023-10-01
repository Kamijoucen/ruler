package com.kamijoucen.ruler.value;

import java.util.HashMap;
import java.util.Map;

public class RsonValue extends AbstractValue implements Rson {

    private Map<String, BaseValue> fields;

    public RsonValue() {
        this(new HashMap<>());
    }
    
    public RsonValue(Map<String, BaseValue> fields) {
        this.fields = fields;
    }

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
