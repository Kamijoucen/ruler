package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class RClassInfo {

    private final Map<String, BaseValue> properties;

    public RClassInfo() {
        this.properties = new HashMap<String, BaseValue>();
    }

    public void put(String name, BaseValue value) {
        this.properties.put(name, value);
    }

    public BaseValue get(String name) {
        return this.properties.get(name);
    }

}
