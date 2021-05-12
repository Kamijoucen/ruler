package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private Scope parent;

    private Map<String, BaseValue> returnSpace;

    private final Map<String, BaseValue> space = new HashMap<String, BaseValue>();

    public Scope(Scope parent) {
        this.parent = parent;
    }

    private boolean isContains(String name) {
        if (space.containsKey(name)) {
            return true;
        } else if (parent != null) {
            return parent.isContains(name);
        }
        return false;
    }

    public BaseValue find(NameAST name) {
        BaseValue baseValue = space.get(name.name.name);
        if (baseValue == null && parent != null) {
            return parent.find(name);
        }
        return baseValue;
    }

    public void put(NameAST name, BaseValue baseValue) {
        if (parent != null && parent.isContains(name.name.name)) {
            parent.put(name, baseValue);
        } else {
            space.put(name.name.name, baseValue);
        }
    }

    public void putReturnValue(String name, BaseValue value) {
        if (this.returnSpace == null) {
            if (parent != null) {
                parent.putReturnValue(name, value);
            }
        } else {
            this.returnSpace.put(name, value);
        }
    }

    public Map<String, BaseValue> getReturnSpace() {
        return returnSpace;
    }

    public void setReturnSpace() {
        this.returnSpace = new HashMap<String, BaseValue>();
    }
}
