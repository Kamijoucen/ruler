package com.kamijoucen.ruler.domain.runtime;

import com.kamijoucen.ruler.domain.type.RulerType;

import java.util.HashMap;
import java.util.Map;

public class TypeScope {

    private final Map<String, RulerType> types = new HashMap<>();
    private final TypeScope parent;

    public TypeScope(TypeScope parent) {
        this.parent = parent;
    }

    public TypeScope getParent() {
        return parent;
    }

    public void put(String name, RulerType type) {
        types.put(name, type);
    }

    public RulerType find(String name) {
        RulerType type = types.get(name);
        if (type != null) {
            return type;
        }
        if (parent != null) {
            return parent.find(name);
        }
        return null;
    }

}
