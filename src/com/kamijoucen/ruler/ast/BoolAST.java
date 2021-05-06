package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.Value;

public class BoolAST implements BaseAST {

    public final Token token;

    public final boolean value;

    public BoolAST(Token token) {
        this.token = token;
        this.value = Boolean.parseBoolean(token.name);
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }
}
