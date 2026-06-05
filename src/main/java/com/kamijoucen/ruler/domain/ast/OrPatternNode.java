package com.kamijoucen.ruler.domain.ast.factor;

import java.util.List;

public class OrPatternNode implements PatternNode {

    private final List<PatternNode> alternatives;

    public OrPatternNode(List<PatternNode> alternatives) {
        this.alternatives = alternatives;
    }

    public List<PatternNode> getAlternatives() {
        return alternatives;
    }

}
