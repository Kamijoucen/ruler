package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope {

    private String stackName;

    private Scope parentScope;

    private List<BaseValue> returnSpace;

    private Map<String, BaseValue> valueSpace;

    public Scope(String stackName, Scope parentScope) {
        this.stackName = stackName;
        this.parentScope = parentScope;
        this.valueSpace = new HashMap<String, BaseValue>();
    }

    public BaseValue find(String name) {
        BaseValue value = valueSpace.get(name);
        if (value != null) {
            return value;
        } else if (parentScope != null) {
            return parentScope.find(name);
        } else {
            return null;
        }
    }

    public void update(String name, BaseValue value) {
        if (valueSpace.containsKey(name)) {
            putLocal(name, value);
        } else if (parentScope != null) {
            parentScope.update(name, value);
        } else {
            throw SyntaxException.withSyntax(name + " 未定义");
        }
    }

    public void putLocal(String name, BaseValue value) {
        valueSpace.put(name, value);
    }

    public boolean hasLocalReturnSpace() {
        return returnSpace != null;
    }

    public void initReturnSpace() {
        returnSpace = new ArrayList<BaseValue>();
    }

    public void putReturnValues(List<BaseValue> values) {
        if (hasLocalReturnSpace()) {
            this.returnSpace.addAll(values);
        } else if (parentScope != null) {
            this.parentScope.putReturnValues(values);
        } else {
            throw SyntaxException.withSyntax("未找到返回值空间");
        }
    }

    public List<BaseValue> getReturnSpace() {
        return returnSpace;
    }
}