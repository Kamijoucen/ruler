package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.value.Value;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private Scope parent;

    private final Map<String, Value> space = new HashMap<String, Value>();

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Value find(NameAST name) {
        Value value = space.get(name.name.name);
        if (value == null && parent != null) {
            return parent.find(name);
        }
        return value;
    }
}
