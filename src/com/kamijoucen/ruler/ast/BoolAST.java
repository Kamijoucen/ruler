package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.value.BaseValue;

public class BoolAST implements BaseAST {

    private boolean value;

    public BoolAST(boolean value) {
        this.value = value;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
