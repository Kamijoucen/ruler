package com.kamijoucen.ruler.domain.ast.factor;

public class TypeofPatternNode implements PatternNode {

    private final String expectedType;

    public TypeofPatternNode(String expectedType) {
        this.expectedType = expectedType;
    }

    public String getExpectedType() {
        return expectedType;
    }

}
