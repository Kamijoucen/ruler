package com.kamijoucen.ruler.domain.ast.factor;

import com.kamijoucen.ruler.domain.ast.BaseNode;

public class LiteralPatternNode implements PatternNode {

    private final BaseNode literal;

    public LiteralPatternNode(BaseNode literal) {
        this.literal = literal;
    }

    public BaseNode getLiteral() {
        return literal;
    }

}
