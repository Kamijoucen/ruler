package com.kamijoucen.ruler.value;

import java.util.HashMap;
import java.util.Map;

public class RsonValue extends AbstractValue {

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

    public Map<String, BaseValue> getFields() {
        return fields;
    }

}
