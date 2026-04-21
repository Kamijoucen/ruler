package com.kamijoucen.ruler.domain.runtime;

import com.kamijoucen.ruler.domain.common.Constant;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private final boolean isCallScope;
    private final TokenLocation callLocation;
    private final String stackName;
    private final Scope parentScope;
    private final Map<String, BaseValue> valueSpace;

    public Scope(String stackName, boolean isCallScope, Scope parentScope, TokenLocation callLocation) {
        this.stackName = stackName;
        this.parentScope = parentScope;
        this.valueSpace = new HashMap<>();
        this.isCallScope = isCallScope;
        this.callLocation = callLocation;
    }

    public String getStackName() {
        return stackName;
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
        if (Constant.isReservedName(name)) {
            throw new RulerRuntimeException("cannot reassign reserved variable: " + name);
        }
        if (valueSpace.containsKey(name)) {
            putLocal(name, value);
        } else if (parentScope != null) {
            parentScope.update(name, value);
        } else {
            throw new RulerRuntimeException("variable not defined: " + name);
        }
    }

    public void defineLocal(String name, BaseValue value) {
        if (Constant.isReservedName(name)) {
            throw new RulerRuntimeException("cannot define reserved variable: " + name);
        }
        if (valueSpace.containsKey(name)) {
            throw new RulerRuntimeException("variable already defined: " + name);
        }
        putLocal(name, value);
    }

    public BaseValue getByLocal(String name) {
        return valueSpace.get(name);
    }

    public void putLocal(String name, BaseValue value) {
        valueSpace.put(name, value);
    }

    public void remove(String name) {
        valueSpace.remove(name);
    }

    public boolean isCallScope() {
        return isCallScope;
    }

    public TokenLocation getCallLocation() {
        return callLocation;
    }
}
