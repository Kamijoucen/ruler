package com.kamijoucen.ruler.domain.ast.expression;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.type.RulerType;
import com.kamijoucen.ruler.domain.value.BaseValue;

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
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public RulerType typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public BaseNode getScrutinee() {
        return scrutinee;
    }

    public List<MatchCase> getCases() {
        return cases;
    }

}
