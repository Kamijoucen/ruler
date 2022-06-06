package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class RMateInfo {

    private BaseValue source;
    private final Map<String, BaseValue> properties;

    public RMateInfo() {
        this.properties = new HashMap<String, BaseValue>();
    }

    public RMateInfo(BaseValue source) {
        this();
        this.source = source;
    }

    public void put(String name, BaseValue value) {
        this.properties.put(name, value);
    }

    public BaseValue get(String name) {
        return this.properties.get(name);
    }

    public BaseValue getSource() {
        return source;
    }

    public void setSource(BaseValue source) {
        this.source = source;
    }
}
