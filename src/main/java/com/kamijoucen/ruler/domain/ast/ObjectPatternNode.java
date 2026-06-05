package com.kamijoucen.ruler.domain.ast.factor;

import java.util.List;

public class ObjectPatternNode implements PatternNode {

    private final List<ObjectPatternField> fields;
    private final RestPatternNode restPattern;

    public ObjectPatternNode(List<ObjectPatternField> fields, RestPatternNode restPattern) {
        this.fields = fields;
        this.restPattern = restPattern;
    }

    public List<ObjectPatternField> getFields() {
        return fields;
    }

    public RestPatternNode getRestPattern() {
        return restPattern;
    }

}
