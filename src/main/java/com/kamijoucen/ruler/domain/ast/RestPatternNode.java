package com.kamijoucen.ruler.domain.ast;

public class RestPatternNode implements PatternNode {

    private final String name;

    public RestPatternNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
