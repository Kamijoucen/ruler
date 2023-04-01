package com.kamijoucen.ruler.value;

import java.util.HashMap;
import java.util.Map;

public class RsonValue extends AbstractValue {

    private final Map<String, BaseValue> fields = new HashMap<String, BaseValue>();


    public RsonValue(RClass classValue) {
        super(classValue);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }

    public Map<String, BaseValue> getFields() {
        return fields;
    }

}
