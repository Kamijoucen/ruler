package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextScope {

    private String stackName;

    private List<BaseValue> returnSpace;

    private Map<String, BaseValue> valueSpace;

    public ContextScope(String stackName) {
        this.stackName = stackName;
        this.valueSpace = new HashMap<String, BaseValue>();
    }

    public boolean hasReturnSpace() {
        return returnSpace != null;
    }

    public void putReturnSpace(List<BaseValue> values) {
        this.returnSpace.addAll(values);
    }

    public void initReturnSpace() {
        returnSpace = new ArrayList<BaseValue>();
    }

    public BaseValue find(String name) {
        return valueSpace.get(name);
    }

    public void put(String name, BaseValue value) {
        valueSpace.put(name, value);
    }

    public boolean containsName(String name) {
        return valueSpace.containsKey(name);
    }

}
