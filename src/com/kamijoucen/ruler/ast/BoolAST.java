package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.BaseValue;

public class BoolAST implements BaseAST {

    private Token token;

    private boolean value;

    public BoolAST(Token token) {
        this.token = token;
        this.value = Boolean.parseBoolean(token.name);
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }
}
