package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class TypeOfNode extends AbstractBaseNode {

    private BaseNode exp;

    public TypeOfNode(BaseNode exp) {
        this.exp = exp;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope, context);
    }

    public BaseNode getExp() {
        return exp;
    }

    public void setExp(BaseNode exp) {
        this.exp = exp;
    }
}
