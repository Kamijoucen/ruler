package com.kamijoucen.ruler.domain.ast;


public class MatchCase {

    private final PatternNode pattern;
    private final BaseNode guard;
    private final BaseNode body;

    public MatchCase(PatternNode pattern, BaseNode guard, BaseNode body) {
        this.pattern = pattern;
        this.guard = guard;
        this.body = body;
    }

    public PatternNode getPattern() {
        return pattern;
    }

    public BaseNode getGuard() {
        return guard;
    }

    public BaseNode getBody() {
        return body;
    }

}
