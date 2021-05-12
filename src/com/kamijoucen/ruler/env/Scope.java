package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private Scope parent;

    private final Map<String, BaseValue> space = new HashMap<String, BaseValue>();

    public Scope(Scope parent) {
        this.parent = parent;
    }

    private boolean isContains(String name) {
        if (parent != null) {
            return parent.isContains(name);
        }
        return space.containsKey(name);
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
}
