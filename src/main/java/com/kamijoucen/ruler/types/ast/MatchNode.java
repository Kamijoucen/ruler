package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

import java.util.List;

public class MatchNode extends AbstractBaseNode {

    private final BaseNode scrutinee;
    private final List<MatchCase> cases;

    public MatchNode(BaseNode scrutinee, List<MatchCase> cases, TokenLocation location) {
        super(location);
        this.scrutinee = scrutinee;
        this.cases = cases;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public BaseNode getScrutinee() {
        return scrutinee;
    }

    public List<MatchCase> getCases() {
        return cases;
    }

}
