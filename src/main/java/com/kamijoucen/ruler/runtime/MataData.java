package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class MataData {

    private Map<String, BaseValue> properties;

    public MataData() {
        this.properties = new HashMap<String, BaseValue>();
    }

    public void put(String name, BaseValue value) {
        this.properties.put(name, value);
    }

    public BaseValue get(String name) {
        return this.properties.get(name);
    }
    
}
