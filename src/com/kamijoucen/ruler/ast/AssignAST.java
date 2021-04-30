package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.Value;

public class AssignAST implements BaseAST {

    public final NameAST name;

    public final BaseAST expression;

    public AssignAST(NameAST name, BaseAST expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }
}
