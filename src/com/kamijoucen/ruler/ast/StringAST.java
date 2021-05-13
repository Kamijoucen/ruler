package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class StringAST implements BaseAST {

    private String value;

    public StringAST(String value) {
        this.value = value;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
