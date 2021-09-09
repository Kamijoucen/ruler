package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public class RuntimeContext {

    private Map<String, BaseValue> outSpace;

    public RuntimeContext(Map<String, BaseValue> outSpace) {
        this.outSpace = outSpace;
    }

    public BaseValue findOutValue(String name) {
        return outSpace.get(name);
    }

}
