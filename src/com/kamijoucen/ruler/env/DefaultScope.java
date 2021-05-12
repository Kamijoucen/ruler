package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class DefaultScope implements Scope {

    private Scope parent;

    private Map<String, BaseValue> returnSpace;

    private Map<String, BaseValue> space;

    private Map<String, RulerFunction> functionSpace;

    public DefaultScope(Scope parent) {
        this.parent = parent;
        this.space = new HashMap<String, BaseValue>();
        this.functionSpace = new HashMap<String, RulerFunction>();
    }

    public DefaultScope(Scope parent, Map<String, BaseValue> space, Map<String, RulerFunction> functionMap) {
        this.parent = parent;
        this.space = space;
        this.functionSpace = functionMap;
    }

    public boolean isContains(String name) {
        if (space.containsKey(name)) {
            return true;
        } else if (parent != null) {
            return parent.isContains(name);
        }
        return false;
    }

    @Override
    public BaseValue find(NameAST name) {
        BaseValue baseValue = space.get(name.name.name);
        if (baseValue == null && parent != null) {
            return parent.find(name);
        }
        return baseValue;
    }

    @Override
    public void put(NameAST name, BaseValue baseValue) {
        if (parent != null && parent.isContains(name.name.name)) {
            parent.put(name, baseValue);
        } else {
            space.put(name.name.name, baseValue);
        }
    }

    @Override
    public void putReturnValue(String name, BaseValue value) {
        if (this.returnSpace == null) {
            if (parent != null) {
                parent.putReturnValue(name, value);
            }
        } else {
            this.returnSpace.put(name, value);
        }
    }

    @Override
    public Map<String, BaseValue> getReturnSpace() {
        return returnSpace;
    }

    @Override
    public void setReturnSpace() {
        this.returnSpace = new HashMap<String, BaseValue>();
    }

    @Override
    public RulerFunction findFunction(String name) {
        RulerFunction function = functionSpace.get(name);
        if (function == null && parent != null) {
            return parent.findFunction(name);
        }
        return function;
    }
}
