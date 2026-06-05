package com.kamijoucen.ruler.domain.ast.factor;

public class ObjectPatternField {

    private final String fieldName;
    private final PatternNode pattern;

    public ObjectPatternField(String fieldName, PatternNode pattern) {
        this.fieldName = fieldName;
        this.pattern = pattern;
    }

    public String getFieldName() {
        return fieldName;
    }

    public PatternNode getPattern() {
        return pattern;
    }

}
