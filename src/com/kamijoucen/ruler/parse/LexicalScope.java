package com.kamijoucen.ruler.parse;

import java.util.HashSet;
import java.util.Set;

public class LexicalScope {

    private Set<String> names;

    public LexicalScope() {
        names = new HashSet<String>();
    }

    public boolean isContains(String name) {
        return names.contains(name);
    }

    public void addName(String name) {
        names.add(name);
    }
}
