package com.kamijoucen.ruler.value;

import java.util.HashMap;
import java.util.Map;

public class RsonValue extends AbstractValue {

    private final Map<String, BaseValue> fields = new HashMap<String, BaseValue>();

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

    public Map<String, BaseValue> getFields() {
        return fields;
    }

}
