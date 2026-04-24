package com.kamijoucen.ruler.domain.value;

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

    @Override
    public String toString() {
        if (fields.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, BaseValue> entry : fields.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

}
