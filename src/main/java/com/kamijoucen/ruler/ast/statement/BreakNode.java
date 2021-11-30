package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class BreakNode extends AbstractBaseNode {

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope, context);
    }

}
