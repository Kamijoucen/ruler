package com.kamijoucen.ruler.domain.ast.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.PatternNode;

public class MatchCase {

    private final PatternNode pattern;
    private final BaseNode body;

    public MatchCase(PatternNode pattern, BaseNode body) {
        this.pattern = pattern;
        this.body = body;
    }

    public PatternNode getPattern() {
        return pattern;
    }

    public BaseNode getBody() {
        return body;
    }

}
