package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.Value;

public class AssignAST implements BaseAST {

    private NameAST name;

    private BaseAST expression;

    public AssignAST(NameAST name, BaseAST expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }

    public NameAST getName() {
        return name;
    }

    public void setName(NameAST name) {
        this.name = name;
    }

    public BaseAST getExpression() {
        return expression;
    }

    public void setExpression(BaseAST expression) {
        this.expression = expression;
    }
}
