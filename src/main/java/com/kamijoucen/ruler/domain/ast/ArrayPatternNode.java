package com.kamijoucen.ruler.domain.ast.factor;

import java.util.List;

public class ArrayPatternNode implements PatternNode {

    private final List<PatternNode> elements;
    private final RestPatternNode restPattern;

    public ArrayPatternNode(List<PatternNode> elements, RestPatternNode restPattern) {
        this.elements = elements;
        this.restPattern = restPattern;
    }

    public List<PatternNode> getElements() {
        return elements;
    }

    public RestPatternNode getRestPattern() {
        return restPattern;
    }

}
