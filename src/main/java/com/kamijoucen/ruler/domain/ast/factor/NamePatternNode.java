package com.kamijoucen.ruler.domain.ast.factor;

public class NamePatternNode implements PatternNode {

    private final String name;

    public NamePatternNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
