package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class DoubleAST implements BaseAST {

    public double value;

    public DoubleAST(double value) {
        this.value = value;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
