package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class BreakAST implements BaseAST {

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }
}
